package uz.davrbank.bustreasury.service.cases.user.rubl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.bustreasury.aggregate.dto.request.TreasuryRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.request.user.CheckStatusRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.request.user.OperationUserRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.ResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.ResultDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.cbu.CurrencyRateResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.user.CheckStatusResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.user.OperationUserResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.Payment;
import uz.davrbank.bustreasury.aggregate.model.Template;
import uz.davrbank.bustreasury.aggregate.model.Wiring;
import uz.davrbank.bustreasury.aggregate.model.user.Operations;
import uz.davrbank.bustreasury.aggregate.model.user.SentWiring;
import uz.davrbank.bustreasury.repository.PaymentRepository;
import uz.davrbank.bustreasury.repository.TemplateRepository;
import uz.davrbank.bustreasury.repository.WiringRepository;
import uz.davrbank.bustreasury.repository.user.OperationsRepository;
import uz.davrbank.bustreasury.repository.user.SentWiringRepository;
import uz.davrbank.bustreasury.service.CallerService;
import uz.davrbank.bustreasury.service.cases.PaymentService;
import uz.davrbank.bustreasury.service.cases.cbu.CbuRateService;
import uz.davrbank.bustreasury.service.interfaces.user.CheckStatusInterface;
import uz.davrbank.bustreasury.service.interfaces.user.WiringInterfaceUSER;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service("wiringServiceUSERSellRUB")
public class WiringServiceUSERSellRUB implements WiringInterfaceUSER {

    private static final Logger LOG = LoggerFactory.getLogger(WiringServiceUSERSellRUB.class);

    private static final ZoneId TASHKENT = ZoneId.of("+05:00");
    private static final int STATUS_CHECK_RETRIES = 3;
    private static final long STATUS_CHECK_DELAY_MS = 5 * 60 * 1000L;  // 5 минут
    private static final int RESEND_MAX = 3;
    private static final long SLEEP_AFTER_FIRST_MS = 5 * 60 * 1000L;   // 5 минут
    private static final boolean SEND_UZS_IN_MINOR_UNITS = true;

    private final uz.davrbank.bustreasury.service.interfaces.WiringInterface wiringInterface;
    private final CallerService callerService;
    private final WiringRepository wiringRepository;
    private final SentWiringRepository sentWiringRepository;
    private final OperationsRepository operationsRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final TemplateRepository templateRepository;
    private final CheckStatusInterface checkStatusInterface;
    private final CbuRateService cbuRateService;

    /** Последовательный 4-значный номер документа */
    private final AtomicLong seq4 = new AtomicLong(0L);

    private synchronized String nextNumericDocNumber() {
        long s = seq4.incrementAndGet();
        if (s > 9999) { seq4.set(1L); s = 1L; }
        return String.format("%04d", s);
    }

    public WiringServiceUSERSellRUB(
            uz.davrbank.bustreasury.service.interfaces.WiringInterface wiringInterface,
            CallerService callerService,
            WiringRepository wiringRepository,
            SentWiringRepository sentWiringRepository,
            OperationsRepository operationsRepository,
            PaymentRepository paymentRepository,
            PaymentService paymentService,
            TemplateRepository templateRepository,
            CheckStatusInterface checkStatusInterface,
            CbuRateService cbuRateService
    ) {
        this.wiringInterface = wiringInterface;
        this.callerService = callerService;
        this.wiringRepository = wiringRepository;
        this.sentWiringRepository = sentWiringRepository;
        this.operationsRepository = operationsRepository;
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
        this.templateRepository = templateRepository;
        this.checkStatusInterface = checkStatusInterface;
        this.cbuRateService = cbuRateService;
    }

    /** Текущая дата (dd.MM.yyyy) */
    private Date todayDate() {
        try {
            LocalDate today = LocalDate.now(TASHKENT);
            return new SimpleDateFormat("dd.MM.yyyy")
                    .parse(today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сформировать сегодняшнюю дату", e);
        }
    }

    /** UZS → строка, при необходимости в тийинах */
    private String toUzs(BigDecimal somAmount) {
        if (somAmount == null) return "0";
        BigDecimal a2 = somAmount.setScale(2, RoundingMode.HALF_UP);
        if (SEND_UZS_IN_MINOR_UNITS) {
            BigDecimal minor = a2.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
            return minor.toPlainString();
        } else {
            return a2.toPlainString();
        }
    }

    /** FX (RUB) → строка в копейках (minor units) */
    private String toFx(String fxAmountStr) {
        if (fxAmountStr == null || fxAmountStr.isBlank()) return "0";
        BigDecimal fx = new BigDecimal(fxAmountStr.trim()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal minor = fx.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
        return minor.toPlainString();
    }

    /** |CBU - Биржевой курс| * Объём RUB */
    private BigDecimal calcRevalUzs(BigDecimal courseSell, BigDecimal cbu, BigDecimal amountFx) {
        BigDecimal diff = cbu.subtract(courseSell).abs();
        return diff.multiply(amountFx).setScale(2, RoundingMode.HALF_UP);
    }

    /** Курс ЦБ по RUB (код 643) */
    private BigDecimal getCbuRubRateOrThrow() {
        CurrencyRateResponseDTO[] rates = cbuRateService.getLatestRateUpdate();
        if (rates == null || rates.length == 0) {
            throw new IllegalArgumentException("Список курсов ЦБ пуст");
        }
        for (CurrencyRateResponseDTO r : rates) {
            if (r == null) continue;
            boolean isRub = "643".equals(r.getCode()) || "RUB".equalsIgnoreCase(r.getCcy());
            if (isRub) {
                String rateStr = r.getRate();
                if (rateStr == null || rateStr.isBlank()) {
                    throw new IllegalArgumentException("Пустое значение курса RUB от ЦБ");
                }
                return new BigDecimal(rateStr.trim());
            }
        }
        throw new IllegalArgumentException("Курс RUB (код 643) не найден среди курсов ЦБ");
    }

    /**
     * Маппинг сумм по wiringId в соответствии с SELL (RUB, T+1).
     */
    private TreasuryRequestDTO buildDTO(WiringResponseDTO w, OperationUserRequestDTO req,
                                        BigDecimal sumUzs, BigDecimal commUzs, BigDecimal revalUzs) {
        TreasuryRequestDTO dto = new TreasuryRequestDTO();
        dto.setDocumentType(w.getDocType().getKodDoc());
        dto.setPaymentCode(w.getPayment().getCode());
        dto.setPurposeId(w.getPurpose().getCode());
        dto.setPurpose(w.getPurpose().getPurpose());
        dto.setPaymentType(1L);
        dto.setTypePlate(w.getTypePlate().getCode());
        dto.setState(1);
        dto.setDate(todayDate());

        int idx = Math.toIntExact(w.getWiringId());
        String debitMfo = w.getDebitMFO().getCode();
        String creditMfo = (w.getCreditMFO() != null) ? w.getCreditMFO().getCode() : null;

        switch (idx) {
            case 1 -> { // OB ПРИХОД RUB (проданные RUB, T-кун)
                dto.setBranch(debitMfo);
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toFx(req.getSumma())); // RUB (копейки)
                LOG.info("[DTO:1] Внебалансовый приход RUB (SELL). FX={}", dto.getSum());
            }
            case 2 -> { // Перечисление RUB на УзРВБ (FX миқдор)
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toFx(req.getSumma()));
                LOG.info("[DTO:2] Перечисление RUB на УзРВБ (SELL). FX={}", dto.getSum());
            }
            case 3 -> { // Поступление UZS NET от УзРВБ = sumUzs - commUzs
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                BigDecimal netUzs = sumUzs.subtract(commUzs);
                dto.setSum(toUzs(netUzs));
                LOG.info("[DTO:3] Поступление UZS NET от УзРВБ. UZS={}", dto.getSum());
            }
            case 4 -> { // Списание UZS ГРУТТО с валютной позиции = sumUzs
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toUzs(sumUzs));
                LOG.info("[DTO:4] Списание UZS брутто с позиции. UZS={}", dto.getSum());
            }
            case 5 -> { // Списание RUB с валютной позиции = FX
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toFx(req.getSumma()));
                LOG.info("[DTO:5] Списание RUB с валютной позиции. FX={}", dto.getSum());
            }
            case 6 -> { // Комиссия биржи → расходы банка = commUzs
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toUzs(commUzs));
                LOG.info("[DTO:6] Комиссия биржи к расходам. UZS={}", dto.getSum());
            }
            case 7 -> { // Переоценка — прибыль (если CBU > курс продажи)
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toUzs(revalUzs));
                LOG.info("[DTO:7] Переоценка (прибыль). UZS={}", dto.getSum());
            }
            case 8 -> { // Переоценка — убыток (если CBU < курс продажи)
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toUzs(revalUzs));
                LOG.info("[DTO:8] Переоценка (убыток). UZS={}", dto.getSum());
            }
            case 9 -> { // OB РАСХОД RUB (закрытие внебалансового учёта)
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toFx(req.getSumma()));
                LOG.info("[DTO:9] Внебалансовый расход RUB (SELL). FX={}", dto.getSum());
            }
            default -> {
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(req.getSumma());
                LOG.warn("[DTO:{}] Неизвестный wiringId — применён маппинг по умолчанию.", idx);
            }
        }
        return dto;
    }

    /** Отправка одной проводки → лог → журнал отправок → статус/TRANS_ID */
    private OperationUserResponseDTO sendOnce(WiringResponseDTO w, OperationUserRequestDTO req,
                                              Operations ops, Payment pay,
                                              BigDecimal sumUzs, BigDecimal commUzs, BigDecimal revalUzs) {
        String pDocNum = nextNumericDocNumber();
        TreasuryRequestDTO dto = buildDTO(w, req, sumUzs, commUzs, revalUzs);
        dto.setDocumentNumber(pDocNum);
        w.setDocumentNumber(pDocNum);

        LOG.info("==> Отправка в шлюз: wiringId={}, docNum={}, sum={}, branch={}",
                w.getWiringId(), pDocNum, dto.getSum(), dto.getBranch());

        try {
            ResultDTO gw = callerService.SendDoc2Gateway(dto, TreasuryRequestDTO.class);
            if (gw.getResponses() == null || gw.getResponses().isEmpty()) {
                LOG.error("<== Пустой ответ шлюза, wiringId={} (docNum={})", w.getWiringId(), pDocNum);
                SentWiring sw = toSentWiring(ops, pay, w, null, "GATEWAY_EMPTY", "No responses", createdBy(req));
                sentWiringRepository.save(sw);
                return buildErr("GATEWAY_EMPTY", "No responses from gateway");
            }
            ResponseDTO r = gw.getResponses().get(0);
            String transId = extractTransId(r);

            LOG.info("<== Ответ шлюза wiringId={} (docNum={}): ERROR_CODE={}, ERROR_NOTE={}, TRANS_ID={}",
                    w.getWiringId(), pDocNum, r.getErrorCode(), r.getErrorNote(), transId);

            SentWiring sw = toSentWiring(ops, pay, w, transId, r.getErrorCode(), r.getErrorNote(), createdBy(req));
            sentWiringRepository.save(sw);

            if (isSuccess(r)) return buildOk(transId);
            return buildErr(r.getErrorCode(), r.getErrorNote());
        } catch (Exception ex) {
            LOG.error("<== Ошибка отправки wiringId={} (docNum={}): {}", w.getWiringId(), pDocNum, ex.getMessage(), ex);
            SentWiring sw = toSentWiring(ops, pay, w, null, "SEND_EXCEPTION", ex.getMessage(), createdBy(req));
            sentWiringRepository.save(sw);
            return buildErr("SEND_EXCEPTION", ex.getMessage());
        }
    }

    private boolean isProveden(String transId) {
        if (transId == null || transId.isBlank()) {
            LOG.warn("Проверка статуса: пустой TRANS_ID");
            return false;
        }
        CheckStatusRequestDTO sreq = new CheckStatusRequestDTO();
        sreq.setP_TRANS_ID(transId);
        sreq.setP_GROUP_ID("0");

        for (int i = 1; i <= STATUS_CHECK_RETRIES; i++) {
            try {
                LOG.info("Проверка статуса (попытка {} из {}), TRANS_ID={}", i, STATUS_CHECK_RETRIES, transId);
                Object resp = checkStatusInterface.getTransactionStatus(sreq);
                CheckStatusResponseDTO st = parseStatus(resp);
                LOG.info("Статус TRANS_ID={}: errorCode={}, stateName={}",
                        transId, (st != null ? st.getERROR_CODE() : "null"), (st != null ? st.getStateName() : "null"));

                if (st != null && "0".equals(st.getERROR_CODE())
                        && "Проведен".equalsIgnoreCase(st.getStateName())) {
                    LOG.info("Статус 'Проведен' подтверждён, TRANS_ID={}", transId);
                    return true;
                }
            } catch (Exception e) {
                LOG.warn("Ошибка при проверке статуса (попытка {}): {}", i, e.getMessage());
            }
            if (i < STATUS_CHECK_RETRIES) {
                LOG.info("Ждём {} мс до следующей проверки, TRANS_ID={}", STATUS_CHECK_DELAY_MS, transId);
                sleepQuiet(STATUS_CHECK_DELAY_MS);
            }
        }
        LOG.warn("'Проведен' не подтверждён, TRANS_ID={}", transId);
        return false;
    }

    private CheckStatusResponseDTO parseStatus(Object response) {
        if (response instanceof List<?> list && !list.isEmpty()) {
            Object f = list.get(0);
            if (f instanceof CheckStatusResponseDTO dto) return dto;
        }
        return null;
    }

    private String extractTransId(ResponseDTO response) {
        return response.getTransId() != null && !"0".equals(response.getTransId()) ? response.getTransId() : null;
    }

    private boolean isSuccess(ResponseDTO response) {
        return "0".equals(response.getErrorCode()) && "SUCCESS".equalsIgnoreCase(response.getErrorNote());
    }

    private String createdBy(OperationUserRequestDTO req) {
        return (req.getCreatedBy() != null && !req.getCreatedBy().isBlank()) ? req.getCreatedBy() : "system";
    }

    private SentWiring toSentWiring(Operations ops, Payment pay, WiringResponseDTO w,
                                    String transId, String errorCode, String errorNote, String createdBy) {
        SentWiring sw = new SentWiring();
        sw.setCreatedBy(createdBy);
        sw.setCreatedDate(LocalDateTime.now(TASHKENT));
        sw.setTransId(transId);
        sw.setErrorCode(errorCode);
        sw.setErrorNote(errorNote);
        sw.setDocumentNumber(w.getDocumentNumber());
        Long templateId = (w.getTemplate() != null ? w.getTemplate().getId() : null);
        Long wiringId = w.getWiringId();
        Wiring wiring = wiringRepository.findByTemplateIdAndWiringId(templateId, wiringId)
                .orElseThrow(() -> new RuntimeException("Wiring not found: " + w.getWiringId()));
        sw.setWiring(wiring);
        sw.setOperations(ops);
        sw.setPayment(pay);
        return sw;
    }

    private Payment ensurePayment() {
        String code = "DEFAULT_PAYMENT_CODE";
        return paymentRepository.findByCode(code).orElseGet(() -> {
            var req = new uz.davrbank.bustreasury.aggregate.dto.request.PaymentRequestDTO(code, "DEFAULT_PAYMENT_NAME");
            var resp = paymentService.createPayment(req);
            return paymentRepository.findById(resp.getId())
                    .orElseThrow(() -> new RuntimeException("Платёж не сохранён"));
        });
    }

    private void sleepQuiet(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }


    @Override
    @Transactional
    public OperationUserResponseDTO createWiringOperations(OperationUserRequestDTO req) {
        String templateId = req.getTemplateId();
        String course = req.getCourse(); // курс продажи (UZS/RUB)
        String summa = req.getSumma();  // объём RUB

        if (templateId == null || templateId.isBlank()) {
            return buildErr("INVALID_TEMPLATE_ID", "Template ID не должен быть пустым");
        }
        if (summa == null || summa.isBlank() || course == null || course.isBlank()) {
            return buildErr("INVALID_INPUT", "Не заполнены SUMMA или COURSE");
        }

        try {
            long tId = Long.parseLong(templateId.trim());
            Template template = templateRepository.findById(tId)
                    .orElseThrow(() -> new IllegalArgumentException("Шаблон не найден: " + tId));

            BigDecimal fxAmount   = new BigDecimal(req.getSumma());   // RUB
            BigDecimal userCourse = new BigDecimal(req.getCourse());  // UZS/RUB (курс продажи)
            BigDecimal cbuObj     = getCbuRubRateOrThrow();           // Курс ЦБ (T+1)

            LOG.info("[SELL RUB T+1] Вход: FX(RUB)={}, USER_COURSE(UZS/RUB)={}, CBU={}", fxAmount, userCourse, cbuObj);

            BigDecimal sumUzs  = fxAmount.multiply(userCourse).setScale(2, RoundingMode.HALF_UP);
            BigDecimal commUzs = sumUzs.multiply(new BigDecimal("0.0004")).setScale(2, RoundingMode.HALF_UP); // 0.04%

            boolean need7 = cbuObj.compareTo(userCourse) > 0; // PROFIT
            boolean need8 = cbuObj.compareTo(userCourse) < 0; // LOSS
            BigDecimal revalUzs = (need7 || need8) ? calcRevalUzs(userCourse, cbuObj, fxAmount) : BigDecimal.ZERO;

            LOG.info("[SELL RUB T+1] SUM_UZS={}, COMM_UZS(0.04%)={}, REVAL_UZS={}, reval={}",
                    sumUzs, commUzs, revalUzs, need7 ? "PROFIT(7)" : (need8 ? "LOSS(8)" : "нет"));

            Operations ops = new Operations();
            ops.setTemplateId(templateId);
            ops.setDate(LocalDateTime.now(TASHKENT));
            ops.setCourse(course);
            ops.setSumma(summa);
            ops = operationsRepository.save(ops);
            LOG.info("[SELL RUB T+1] Operation saved: id={}, templateId={}, course={}, summa={}",
                    ops.getId(), ops.getTemplateId(), ops.getCourse(), ops.getSumma());

            Payment pay = ensurePayment();
            LOG.info("[SELL RUB T+1] Payment ensured: id={}, code={}", pay.getId(), pay.getCode());

            // Загружаем проводки по шаблону
            List<WiringResponseDTO> all = wiringInterface.getAllByTemplateId(template)
                    .stream()
                    .filter(w -> w.getWiringId() != null)
                    .sorted(Comparator.comparingLong(WiringResponseDTO::getWiringId))
                    .toList();
            LOG.info("[SELL RUB T+1] Найдено {} проводок (sorted by wiringId).", all.size());

            // №1 — отдельно + ждём 'Проведен'
            WiringResponseDTO firstWiring = all.stream()
                    .filter(w -> Objects.equals(w.getWiringId(), 1L))
                    .findFirst()
                    .orElse(null);
            if (firstWiring == null) {
                return buildErr("NO_WIRING_1", "Не найдена проводка №1");
            }

            LOG.info("[SELL RUB T+1] Старт №1 (OB приход RUB)...");
            OperationUserResponseDTO firstRes = sendOnce(firstWiring, req, ops, pay, sumUzs, commUzs, revalUzs);
            if (!"0".equals(firstRes.getErrorCode())) {
                LOG.error("[SELL RUB T+1] Проводка №1 не отправлена: {}", firstRes.getErrorNote());
                return buildErr("FIRST_FAILED", "Проводка №1 не отправлена: " + firstRes.getErrorNote());
            }

            boolean ok = checkThenResendIfNeeded(firstWiring, firstRes.getTransId(), req, ops, pay, sumUzs, commUzs, revalUzs);
            if (!ok) {
                LOG.error("[SELL RUB T+1] Проводка №1 не 'Проведен'");
                return buildErr("FIRST_NOT_PROVEDEN", "Проводка №1 не 'Проведен'");
            }

            LOG.info("[SELL RUB T+1] №1 'Проведен'. Ждём {} минут...", SLEEP_AFTER_FIRST_MS / 60000);
            sleepQuiet(SLEEP_AFTER_FIRST_MS);

            // План: 2,3,4,5,6,(7/8),9
            LinkedHashSet<Long> restNeeded = new LinkedHashSet<>(Arrays.asList(2L, 3L, 4L, 5L, 6L));
            if (need7) restNeeded.add(7L);
            else if (need8) restNeeded.add(8L);
            restNeeded.add(9L);

            List<WiringResponseDTO> restPlan = all.stream()
                    .filter(w -> restNeeded.contains(w.getWiringId()))
                    .sorted(Comparator.comparingLong(WiringResponseDTO::getWiringId))
                    .toList();

            List<String> sendFails = new ArrayList<>();
            Map<Long, String> w2Trans = sendBatchNoCheck(restPlan, req, ops, pay, sumUzs, commUzs, revalUzs, sendFails);

            List<Long> checkedOk = new ArrayList<>();
            List<Long> checkedFail = new ArrayList<>();

            for (WiringResponseDTO w : restPlan) {
                String transId = w2Trans.get(w.getWiringId());
                LOG.info("[SELL RUB T+1][CHECK] Проверка wiringId={}, TRANS_ID={}", w.getWiringId(), transId);

                boolean proved = false;
                if (transId != null && !transId.isBlank()) {
                    proved = isProveden(transId);
                } else {
                    LOG.warn("[SELL RUB T+1][CHECK] Нет TRANS_ID — пробуем переслать, wiringId={}", w.getWiringId());
                }

                if (!proved) {
                    LOG.info("[SELL RUB T+1][CHECK] resend+check для wiringId={}", w.getWiringId());
                    proved = checkThenResendIfNeeded(w, transId, req, ops, pay, sumUzs, commUzs, revalUzs);
                }

                if (proved) checkedOk.add(w.getWiringId());
                else checkedFail.add(w.getWiringId());
            }

            OperationUserResponseDTO out = new OperationUserResponseDTO();
            out.setGroupId(0L);

            String allTrans = String.join(",", w2Trans.values().stream().filter(Objects::nonNull).toList());
            out.setTransId(allTrans.isBlank() ? firstRes.getTransId() : firstRes.getTransId() + "," + allTrans);

            StringBuilder note = new StringBuilder("Пакет после №1 отправлен. ");
            if (!sendFails.isEmpty()) note.append("Ошибки отправки=").append(sendFails).append(". ");
            note.append("OK=").append(checkedOk).append(", FAIL=").append(checkedFail);

            if (checkedFail.isEmpty() && sendFails.isEmpty()) {
                out.setErrorCode("0");
                out.setErrorNote("SUCCESS. " + note);
            } else if (checkedOk.isEmpty()) {
                out.setErrorCode("FAILED");
                out.setErrorNote(note.toString());
            } else {
                out.setErrorCode("PARTIAL_SUCCESS");
                out.setErrorNote(note.toString());
            }
            LOG.info("[SELL RUB T+1] RESULT: errorCode={}, errorNote={}", out.getErrorCode(), out.getErrorNote());
            return out;

        } catch (NumberFormatException e) {
            LOG.error("[SELL RUB T+1] Некорректный TEMPLATE_ID: {}", e.getMessage());
            return buildErr("INVALID_TEMPLATE_ID", "Некорректный TEMPLATE_ID: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("[SELL RUB T+1] Непредвиденная ошибка: {}", e.getMessage(), e);
            return buildErr("UNEXPECTED_ERROR", e.getMessage());
        }
    }

    private Map<Long, String> sendBatchNoCheck(
            List<WiringResponseDTO> wires,
            OperationUserRequestDTO req,
            Operations ops,
            Payment pay,
            BigDecimal sumUzs,
            BigDecimal commUzs,
            BigDecimal revalUzs,
            List<String> sendFails
    ) {
        Map<Long, String> w2Trans = new LinkedHashMap<>();
        LOG.info("Старт пакетной отправки: {} проводок (SELL RUB T+1)...", wires.size());
        for (WiringResponseDTO w : wires) {
            LOG.info("[BATCH] Отправка wiringId={} ...", w.getWiringId());
            OperationUserResponseDTO res = sendOnce(w, req, ops, pay, sumUzs, commUzs, revalUzs);
            if ("0".equals(res.getErrorCode())) {
                LOG.info("[BATCH] OK wiringId={}, TRANS_ID={}", w.getWiringId(), res.getTransId());
                w2Trans.put(w.getWiringId(), res.getTransId());
            } else {
                String msg = "wiringId=" + w.getWiringId() + " SEND_FAIL: " + res.getErrorNote();
                LOG.warn("[BATCH] Не удалось отправить: {}", msg);
                sendFails.add(msg);
            }
        }
        LOG.info("Пакет завершён. Успешно: {}, Ошибок отправки: {}", w2Trans.size(), sendFails.size());
        return w2Trans;
    }

    private boolean checkThenResendIfNeeded(WiringResponseDTO w, String initialTransId,
                                            OperationUserRequestDTO req, Operations ops, Payment pay,
                                            BigDecimal sumUzs, BigDecimal commUzs, BigDecimal revalUzs) {
        if (initialTransId != null && isProveden(initialTransId)) {
            LOG.info("[RECHECK] Уже 'Проведен', wiringId={}, TRANS_ID={}", w.getWiringId(), initialTransId);
            return true;
        }

        for (int attempt = 1; attempt <= RESEND_MAX; attempt++) {
            LOG.warn("[RESEND] Попытка {} / {} wiringId={} (ждём {} мс перед повтором)",
                    attempt, RESEND_MAX, w.getWiringId(), STATUS_CHECK_DELAY_MS);
            sleepQuiet(STATUS_CHECK_DELAY_MS);

            OperationUserResponseDTO resend = sendOnce(w, req, ops, pay, sumUzs, commUzs, revalUzs);
            if (!"0".equals(resend.getErrorCode())) {
                LOG.warn("[RESEND] Отправка не удалась, попытка {}: {}", attempt, resend.getErrorNote());
                continue;
            }

            String newTransId = resend.getTransId();
            LOG.info("[RESEND] Повторная отправка успешна, wiringId={}, new TRANS_ID={}", w.getWiringId(), newTransId);

            if (isProveden(newTransId)) {
                LOG.info("[RESEND] 'Проведен' подтверждён, wiringId={}", w.getWiringId());
                return true;
            }
        }
        LOG.error("[RESEND] Не удалось добиться 'Проведен', wiringId={}", w.getWiringId());
        return false;
    }

    private OperationUserResponseDTO buildOk(String transId) {
        OperationUserResponseDTO r = new OperationUserResponseDTO();
        r.setErrorCode("0");
        r.setErrorNote("SUCCESS");
        r.setTransId(transId);
        r.setGroupId(0L);
        return r;
    }

    private OperationUserResponseDTO buildErr(String code, String note) {
        OperationUserResponseDTO r = new OperationUserResponseDTO();
        r.setErrorCode(code);
        r.setErrorNote(note);
        r.setTransId(null);
        r.setGroupId(0L);
        return r;
    }
}
