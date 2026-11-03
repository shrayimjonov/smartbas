package uz.davrbank.bustreasury.service.cases.user.usd;

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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import uz.davrbank.bustreasury.util.FinalState;

@Service("wiringServiceUSERBuy")
public class WiringServiceUSERBuy implements WiringInterfaceUSER {

    private static final Logger LOG = LoggerFactory.getLogger(WiringServiceUSERBuy.class);

    private static final ZoneId TASHKENT = ZoneId.of("+05:00");
    private static final int  STATUS_CHECK_RETRIES_FIRST = 3;
    private static final long STATUS_CHECK_DELAY_MS_FIRST = 5 * 60 * 1000L; // 5 минут
    private static final long SLEEP_AFTER_FIRST_MS = 5 * 60 * 1000L; // 5 минут
    private static final int  RESEND_MAX = 3;
    private static final long RESEND_DELAY_MS = 30 * 1000L; // 30 секунд между ретраями (адекватно продам)
    private static final long POLL_INTERVAL_MS = 2 * 60 * 1000L; // 2 минуты
    private static final int  POLL_MAX_ATTEMPTS = 30;            // до ~60 минут
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

    public WiringServiceUSERBuy(uz.davrbank.bustreasury.service.interfaces.WiringInterface wiringInterface,
                                CallerService callerService,
                                WiringRepository wiringRepository,
                                SentWiringRepository sentWiringRepository,
                                OperationsRepository operationsRepository,
                                PaymentRepository paymentRepository,
                                PaymentService paymentService,
                                TemplateRepository templateRepository,
                                CheckStatusInterface checkStatusInterface,
                                CbuRateService cbuRateService) {
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

    private final AtomicLong seq4 = new AtomicLong(0L);


    private synchronized String nextNumericDocNumber() {
        long s = seq4.incrementAndGet();
        if (s > 9999) { seq4.set(1L); s = 1L; }
        return String.format("%04d", s);
    }
    private Date todayDate() {
        try {
            LocalDate today = LocalDate.now(TASHKENT);
            return new SimpleDateFormat("dd.MM.yyyy")
                    .parse(today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        } catch (Exception e) { throw new RuntimeException("Не удалось сформировать дату 'сегодня'", e); }
    }
    private String toUzs(BigDecimal somAmount) {
        if (somAmount == null) return "0";
        BigDecimal a2 = somAmount.setScale(2, RoundingMode.HALF_UP);
        if (SEND_UZS_IN_MINOR_UNITS) {
            BigDecimal minor = a2.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
            return minor.toPlainString();
        } else return a2.toPlainString();
    }

    private String toFx(String fxAmountStr) {
        if (fxAmountStr == null || fxAmountStr.isBlank()) return "0";
        BigDecimal fx = new BigDecimal(fxAmountStr.trim()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal minor = fx.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
        return minor.toPlainString();
    }

    private BigDecimal calcRevalUzs(BigDecimal course, BigDecimal cbu, BigDecimal amountFx) {
        BigDecimal diff = cbu.subtract(course).abs();
        return diff.multiply(amountFx).setScale(2, RoundingMode.HALF_UP);
    }

    private void sleepQuiet(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } }


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
            case 1 -> { // Внебалансовый приход FX
                dto.setBranch(debitMfo);
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toFx(req.getSumma()));
                LOG.info("[DTO:1] Подготовлена проводка №1 (внебалансовый ПРИХОД FX). FX={} (мин.ед.)", dto.getSum());
            }
            case 2 -> { // Комиссия бирже (UZS)
                dto.setBranch(creditMfo);
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCompanyName(w.getCreditMFO().getName());
                dto.setCompanyInn(w.getCreditInn());
                dto.setCompanyBank(creditMfo);
                dto.setSum(toUzs(commUzs));
                dto.setPurpose("~86020370970712 " + w.getPurpose().getPurpose());
                LOG.info("[DTO:2] Подготовлена комиссия бирже. UZS={} (мин.ед.)", dto.getSum());
            }
            case 3, 4 -> { // Провод FX
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toFx(req.getSumma()));
                LOG.info("[DTO:{}] Подготовлен FX-перевод. FX={} (мин.ед.)", idx, dto.getSum());
            }
            case 5 -> { // Сум-эквивалент (UZS)
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toUzs(sumUzs));
                LOG.info("[DTO:5] Подготовлен сумовой эквивалент. UZS={} (мин.ед.)", dto.getSum());
            }
            case 6 -> { // Комиссия в расходы (UZS)
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toUzs(commUzs));
                dto.setPurpose(w.getPurpose().getPurpose());
                LOG.info("[DTO:6] Комиссия к расходам. UZS={} (мин.ед.)", dto.getSum());
            }
            case 7 -> { // Переоценка прибыль (UZS)
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toUzs(revalUzs));
                dto.setPurpose(w.getPurpose().getPurpose());
                LOG.info("[DTO:7] Переоценка (ПРИБЫЛЬ). UZS={} (мин.ед.)", dto.getSum());
            }
            case 8 -> { // Переоценка убыток (UZS)
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toUzs(revalUzs));
                dto.setPurpose(w.getPurpose().getPurpose());
                LOG.info("[DTO:8] Переоценка (УБЫТОК). UZS={} (мин.ед.)", dto.getSum());
            }
            case 9 -> { // Внебалансовый расход FX
                dto.setBranch(debitMfo);
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(toFx(req.getSumma()));
                LOG.info("[DTO:9] Списание с внебалансового счёта FX. FX={} (мин.ед.)", dto.getSum());
            }
            default -> {
                dto.setDebitAccount(w.getDebitAccountNum());
                dto.setCreditAccount(w.getCreditAccountNum());
                dto.setCompanyName(w.getDebitMFO().getName());
                dto.setCompanyInn(w.getDebitInn());
                dto.setCompanyBank(debitMfo);
                dto.setSum(req.getSumma());
                LOG.warn("[DTO:{}] Неизвестный wiringId — применён дефолтный маппинг. SUM={}", idx, dto.getSum());
            }
        }
        return dto;
    }

    private OperationUserResponseDTO sendOnce(WiringResponseDTO w, OperationUserRequestDTO req,
                                              Operations ops, Payment pay,
                                              BigDecimal sumUzs, BigDecimal commUzs, BigDecimal revalUzs) {
        String pDocNum = nextNumericDocNumber();
        TreasuryRequestDTO dto = buildDTO(w, req, sumUzs, commUzs, revalUzs);
        dto.setDocumentNumber(pDocNum);
        w.setDocumentNumber(pDocNum);

        LOG.info("==> [SEND] Отправка проводки в шлюз: wiringId={}, docNum={}, typeDoc={}, purposeId={}, sum={}, branch={}",
                w.getWiringId(), pDocNum, dto.getDocumentType(), dto.getPurposeId(), dto.getSum(), dto.getBranch());

        try {
            ResultDTO gw = callerService.SendDoc2Gateway(dto, TreasuryRequestDTO.class);
            if (gw.getResponses() == null || gw.getResponses().isEmpty()) {
                LOG.error("<== [SEND] Пустой ответ шлюза, wiringId={} (docNum={})", w.getWiringId(), pDocNum);
                SentWiring sw = toSentWiring(ops, pay, w, null, "GATEWAY_EMPTY", "No responses", createdBy(req));
                sentWiringRepository.save(sw);
                return buildErr("GATEWAY_EMPTY", "No responses from gateway");
            }
            ResponseDTO responseDTO = gw.getResponses().get(0);
            String transId = extractTransId(responseDTO);

            LOG.info("<== [SEND] Ответ шлюза wiringId={} (docNum={}): ERROR_CODE={}, ERROR_NOTE={}, TRANS_ID={}",
                    w.getWiringId(), pDocNum, responseDTO.getErrorCode(), responseDTO.getErrorNote(), transId);

            SentWiring sw = toSentWiring(ops, pay, w, transId, responseDTO.getErrorCode(), responseDTO.getErrorNote(), createdBy(req));
            sentWiringRepository.save(sw);

            if (isSuccess(responseDTO)) return buildOk(transId);
            return buildErr(responseDTO.getErrorCode(), responseDTO.getErrorNote());
        } catch (Exception ex) {
            LOG.error("<== [SEND] Исключение при отправке wiringId={} (docNum={}): {}", w.getWiringId(), pDocNum, ex.getMessage(), ex);
            SentWiring sw = toSentWiring(ops, pay, w, null, "SEND_EXCEPTION", ex.getMessage(), createdBy(req));
            sentWiringRepository.save(sw);
            return buildErr("SEND_EXCEPTION", ex.getMessage());
        }
    }

    private boolean isProvedenFirst(String transId) {
        if (transId == null || transId.isBlank()) {
            LOG.warn("[STATUS-1] Проверка статуса: transId пуст");
            return false;
        }
        CheckStatusRequestDTO sreq = new CheckStatusRequestDTO();
        sreq.setP_TRANS_ID(transId);
        sreq.setP_GROUP_ID("0");

        for (int i = 1; i <= STATUS_CHECK_RETRIES_FIRST; i++) {
            try {
                LOG.info("[STATUS-1] Проверка статуса (попытка {} из {}), TRANS_ID={}",
                        i, STATUS_CHECK_RETRIES_FIRST, transId);
                Object resp = checkStatusInterface.getTransactionStatus(sreq);
                CheckStatusResponseDTO st = parseStatus(resp);

                String state = (st != null ? st.getStateName() : null);
                String error = (st != null ? st.getERROR_CODE() : null);
                LOG.info("[STATUS-1] Статус TRANS_ID={}: ERROR_CODE={}, STATE_NAME={}",
                        transId, (st != null ? st.getERROR_CODE() : "null"), (st != null ? st.getStateName() : "null"));

                if (st != null && "0".equals(error) && "Проведен".equalsIgnoreCase(state)) {
                    LOG.info("[STATUS-1] Подтверждено: «Проведен», TRANS_ID={}", transId);
                    return true;
                }

                if (st != null && "Удален".equalsIgnoreCase(state)){
                    LOG.warn("[STATUS-1] Получено «Удален». Прерываем без повторов. TRANS_ID={}", transId);
                    return false;
                }
            } catch (Exception e) {
                LOG.warn("[STATUS-1] Ошибка проверки статуса (попытка {}): {}", i, e.getMessage());
            }
            if (i < STATUS_CHECK_RETRIES_FIRST) {
                LOG.info("[STATUS-1] Ожидаем {} мс до следующей проверки, TRANS_ID={}",
                        STATUS_CHECK_DELAY_MS_FIRST, transId);
                sleepQuiet(STATUS_CHECK_DELAY_MS_FIRST);
            }
        }
        LOG.warn("[STATUS-1] «Проведен» не подтвержден, TRANS_ID={}", transId);
        return false;
    }

    private Map.Entry<FinalState, CheckStatusResponseDTO> waitUntilFinalWithInterval(String transId) {
        CheckStatusRequestDTO sreq = new CheckStatusRequestDTO();
        sreq.setP_TRANS_ID(transId);
        sreq.setP_GROUP_ID("0");

        CheckStatusResponseDTO last = null;

        for (int i = 1; i <= POLL_MAX_ATTEMPTS; i++) {
            try {
                LOG.info("[POLL] Проверка статуса ({} / {}), TRANS_ID={}", i, POLL_MAX_ATTEMPTS, transId);
                Object resp = checkStatusInterface.getTransactionStatus(sreq);
                last = parseStatus(resp);

                if (last != null && "0".equals(last.getERROR_CODE())) {
                    String stateRaw = String.valueOf(last.getStateName());
                    String state = stateRaw == null ? "" : stateRaw.trim().toLowerCase(Locale.ROOT);
                    LOG.info("[POLL] Ответ шлюза: TRANS_ID={}, STATE_NAME={}", transId, stateRaw);

                    if (state.equals("проведен")) {
                        return Map.entry(FinalState.APPROVED, last);
                    }
                    if (state.equals("удалено") || state.equals("удален")) {
                        return Map.entry(FinalState.DELETED, last);
                    }
                } else {
                    LOG.warn("[POLL] Некорректный ответ/ошибка при проверке статуса, TRANS_ID={}", transId);
                }
            } catch (Exception ex) {
                LOG.warn("[POLL] Исключение при проверке статуса TRANS_ID={}, попытка {}: {}",
                        transId, i, ex.getMessage());
            }

            if (i < POLL_MAX_ATTEMPTS) {
                LOG.info("[POLL] Ожидание {} мс до следующего запроса, TRANS_ID={}", POLL_INTERVAL_MS, transId);
                sleepQuiet(POLL_INTERVAL_MS);
            }
        }
        LOG.warn("[POLL] Таймаут ожидания финального статуса, TRANS_ID={}", transId);
        return Map.entry(FinalState.TIMEOUT, last);
    }


    private LinkedHashMap<Long, Map<String, Object>> checkAllWithSummary(
            List<Long> order,
            Map<Long, String> w2Trans
    ) {
        LinkedHashMap<Long, Map<String, Object>> summary = new LinkedHashMap<>();
        for (Long wid : order) {
            String transId = w2Trans.get(wid);
            if (transId == null || transId.isBlank()) {
                Map<String, Object> one = new LinkedHashMap<>();
                one.put("transId", null);
                one.put("state", FinalState.REJECTED.name());
                one.put("note", "Нет TRANS_ID (сбой на этапе отправки)");
                summary.put(wid, one);
                LOG.warn("[SUMMARY] wiringId={} — пропуск проверки: TRANS_ID отсутствует", wid);
                continue;
            }

            var result = waitUntilFinalWithInterval(transId);
            FinalState fs = result.getKey();
            CheckStatusResponseDTO last = result.getValue();

            Map<String, Object> one = new LinkedHashMap<>();
            one.put("transId", transId);
            one.put("state", fs.name());
            one.put("gatewayState", last != null ? last.getStateName() : null);
            one.put("errorCode", last != null ? last.getERROR_CODE() : null);
            one.put("errorNote", last != null ? last.getERROR_NOTE() : null);


            if (last != null && "Удален".equalsIgnoreCase(last.getStateName()) && fs == FinalState.TIMEOUT) {
                one.put("state", FinalState.DELETED.name());
                fs = FinalState.DELETED;
            }
            summary.put(wid, one);

            tryUpdateSentWiringFinalState(wid, transId, fs);

            LOG.info("[SUMMARY] wiringId={} → FINAL={}, gatewayState={}, transId={}",
                    wid, fs.name(), one.get("gatewayState"), transId);
        }
        return summary;
    }

    private void tryUpdateSentWiringFinalState(Long wiringId, String transId, FinalState fs) {
        try {
            Optional<SentWiring> opt = sentWiringRepository
                    .findTopByWiring_WiringIdAndTransIdOrderByCreatedDateDesc(wiringId, transId);
            if (opt.isPresent()) {
                SentWiring sw = opt.get();
                sw.setFinaleState(fs.name());
                sw.setFinalizeAt(String.valueOf(LocalDateTime.now(TASHKENT)));
                sentWiringRepository.save(sw);
            }
        } catch (Exception e) {
            LOG.warn("[SUMMARY] Не удалось обновить финальный статус в БД: wid={}, transId={}, err={}",
                    wiringId, transId, e.getMessage());
        }
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

        Long templateId = Long.parseLong(ops.getTemplateId());
        Wiring wiring = wiringRepository
                .findByTemplateIdAndWiringId(templateId, w.getWiringId())
                .orElseThrow(() -> new RuntimeException(
                        "Wiring not found by templateId=" + templateId + " and wiringId=" + w.getWiringId()));

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

    private String sendSequentialRequireTransId(WiringResponseDTO w,
                                                OperationUserRequestDTO req,
                                                Operations ops,
                                                Payment pay,
                                                BigDecimal sumUzs, BigDecimal commUzs, BigDecimal revalUzs) {
        for (int attempt = 1; attempt <= RESEND_MAX; attempt++) {
            LOG.info("[SEQ] Отправка wiringId={} (попытка {} из {})", w.getWiringId(), attempt, RESEND_MAX);
            OperationUserResponseDTO res = sendOnce(w, req, ops, pay, sumUzs, commUzs, revalUzs);
            if ("0".equals(res.getErrorCode()) && res.getTransId() != null && !res.getTransId().isBlank()) {
                LOG.info("[SEQ] Успех: получен TRANS_ID={} для wiringId={}", res.getTransId(), w.getWiringId());
                return res.getTransId();
            }
            LOG.warn("[SEQ] Не удалось получить TRANS_ID для wiringId={} ({}: {})",
                    w.getWiringId(), res.getErrorCode(), res.getErrorNote());
            if (attempt < RESEND_MAX) {
                LOG.info("[SEQ] Пауза {} мс перед повторной отправкой wiringId={}", RESEND_DELAY_MS, w.getWiringId());
                sleepQuiet(RESEND_DELAY_MS);
            }
        }
        LOG.error("[SEQ] Ошибка: TRANS_ID не получен после {} попыток, wiringId={}", RESEND_MAX, w.getWiringId());
        return null;
    }

    @Override
    @Transactional
    public OperationUserResponseDTO createWiringOperations(OperationUserRequestDTO req) {
        String templateId = req.getTemplateId();
        String course = req.getCourse();
        String summa = req.getSumma();

        if (templateId == null || templateId.isBlank()) {
            return buildErr("INVALID_TEMPLATE_ID", "Template ID не может быть пустым");
        }
        if (summa == null || summa.isBlank() || course == null || course.isBlank()) {
            return buildErr("INVALID_INPUT", "SUMMA или COURSE не заполнены");
        }

        try {
            long tId = Long.parseLong(templateId.trim());
            Template template = templateRepository.findById(tId)
                    .orElseThrow(() -> new IllegalArgumentException("Шаблон не найден: " + tId));

            BigDecimal fxAmount   = new BigDecimal(req.getSumma());
            BigDecimal userCourse = new BigDecimal(req.getCourse());
            BigDecimal cbuObj     = getCbuUsdRateOrThrow();

            LOG.info("[BUY] Вход: FX={}, COURSE(User)={}, COURSE(CBU)={}", fxAmount, userCourse, cbuObj);

            BigDecimal sumUzs  = fxAmount.multiply(userCourse).setScale(2, RoundingMode.HALF_UP);
            BigDecimal commUzs = sumUzs.multiply(new BigDecimal("0.0004")).setScale(2, RoundingMode.HALF_UP);

            boolean need7 = userCourse.compareTo(cbuObj) < 0;
            boolean need8 = userCourse.compareTo(cbuObj) > 0;
            BigDecimal revalUzs = (need7 || need8) ? calcRevalUzs(userCourse, cbuObj, fxAmount) : BigDecimal.ZERO;

            LOG.info("[BUY] Расчёты: SUM_UZS={}, COMM(0.04%)={}, REVAL_UZS={}, reval={}",
                    sumUzs, commUzs, revalUzs, need7 ? "7-ПРИБЫЛЬ" : (need8 ? "8-УБЫТОК" : "нет"));

            Operations ops = new Operations();
            ops.setTemplateId(templateId);
            ops.setDate(LocalDateTime.now(TASHKENT));
            ops.setCourse(course);
            ops.setSumma(summa);
            ops = operationsRepository.save(ops);
            LOG.info("[BUY] Операция сохранена: id={}, templateId={}, course={}, summa={}",
                    ops.getId(), ops.getTemplateId(), ops.getCourse(), ops.getSumma());

            Payment pay = ensurePayment();
            LOG.info("[BUY] Платёж обеспечен: id={}, code={}", pay.getId(), pay.getCode());

            List<WiringResponseDTO> all = wiringInterface.getAllByTemplateId(template)
                    .stream()
                    .filter(w -> w.getWiringId() != null)
                    .sorted(Comparator.comparingLong(WiringResponseDTO::getWiringId))
                    .toList();
            LOG.info("[BUY] Найдено {} проводок в шаблоне (упорядочено по wiringId).", all.size());

            WiringResponseDTO first = all.stream()
                    .filter(w -> Objects.equals(w.getWiringId(), 1L))
                    .findFirst()
                    .orElse(null);
            if (first == null) {
                return buildErr("NO_WIRING_1", "1-я проводка не найдена");
            }

            LOG.info("[BUY] Старт: отправляем проводку №1...");
            OperationUserResponseDTO firstRes = sendOnce(first, req, ops, pay, sumUzs, commUzs, revalUzs);
            if (!"0".equals(firstRes.getErrorCode()) || firstRes.getTransId() == null) {
                LOG.error("[BUY] Проводка №1 не отправлена/без TRANS_ID: {}", firstRes.getErrorNote());
                return buildErr("FIRST_FAILED", "1-я проводка не отправлена: " + firstRes.getErrorNote());
            }

            LOG.info("[BUY] Проверяем, что проводка №1 действительно «Проведен»...");
            boolean ok = isProvedenFirst(firstRes.getTransId());
            if (!ok) {
                LOG.error("[BUY] Проводка №1 не получила статус «Проведен». Прерываем процесс.");
                return buildErr("FIRST_NOT_PROVEDEN", "1-я проводка не Проведен");
            }

            LOG.info("[BUY] Проводка №1 = «Проведен». Ждём {} мин перед отправкой хвоста...", SLEEP_AFTER_FIRST_MS / 60000);
            sleepQuiet(SLEEP_AFTER_FIRST_MS);

            LinkedHashSet<Long> restOrder = new LinkedHashSet<>(Arrays.asList(2L, 3L, 4L, 5L, 6L));
            if (need7) restOrder.add(7L);
            else if (need8) restOrder.add(8L);
            restOrder.add(9L);

            List<WiringResponseDTO> restPlan = all.stream()
                    .filter(w -> restOrder.contains(w.getWiringId()))
                    .sorted(Comparator.comparingLong(WiringResponseDTO::getWiringId))
                    .toList();

            if (restPlan.isEmpty()) {
                LOG.warn("[BUY] После №1 отправлять нечего — завершаем.");
                OperationUserResponseDTO out = new OperationUserResponseDTO();
                out.setErrorCode("0");
                out.setErrorNote("Только 1-я проводка требовалась — выполнено.");
                out.setGroupId(0L);
                out.setTransId(firstRes.getTransId());

                out.setTotalWires(1);
                out.setApproved(1);
                out.setDeleted(0);
                out.setTimeout(0);
                out.setFailed(0);
                out.setStatusSummary("APPROVED=1, DELETED=0, TIMEOUT=0");
                return out;
            }

            Map<Long, String> w2Trans = new LinkedHashMap<>();
            for (WiringResponseDTO w : restPlan) {
                LOG.info("[SEQ] отправка: wiringId={}", w.getWiringId());
                String transId = sendSequentialRequireTransId(w, req, ops, pay, sumUzs, commUzs, revalUzs);
                if (transId == null) {

                    String sentBefore = w2Trans.entrySet().stream()
                            .map(e -> e.getKey() + ":" + e.getValue())
                            .collect(Collectors.joining(","));
                    LOG.error("[SEQ] Прерывание: не удалось получить TRANS_ID для wiringId={}. Отправленные ранее: [{}]",
                            w.getWiringId(), sentBefore);

                    OperationUserResponseDTO out = new OperationUserResponseDTO();
                    out.setErrorCode("FAILED");
                    out.setErrorNote("Сбой на этапе отправки хвоста. Не получен TRANS_ID для wiringId=" + w.getWiringId());
                    out.setGroupId(0L);
                    out.setTransId(firstRes.getTransId());

                    out.setTotalWires(1 + restPlan.size());
                    out.setApproved(1 + w2Trans.size());
                    out.setDeleted(0);
                    out.setTimeout(0);
                    out.setFailed((1 + restPlan.size()) - out.getApproved());
                    out.setStatusSummary(String.format("APPROVED=%d, DELETED=%d, TIMEOUT=%d",
                            out.getApproved(), out.getDeleted(), out.getTimeout()));
                    return out;
                }
                w2Trans.put(w.getWiringId(), transId);
            }

            LOG.info("[SEQ] Хвост успешно отправлен. TRANS_ID получены для всех {} проводок. Переходим к итоговой проверке.",
                    w2Trans.size());

            List<Long> order = restPlan.stream().map(WiringResponseDTO::getWiringId).toList();
            LinkedHashMap<Long, Map<String, Object>> finalSummary = checkAllWithSummary(order, w2Trans);

            int approved = 0, deleted = 0, timeout = 0, failed = 0;

            for (var e : finalSummary.entrySet()) {
                String state = (String) e.getValue().get("state");
                switch (state) {
                    case "APPROVED" -> approved++;
                    case "DELETED" -> deleted++;
                    case "TIMEOUT" -> timeout++;
                    case "FAILED" -> failed++;
                }
            }

            int totalTail = restPlan.size();
            int total = 1 + totalTail;

            String summaryStr = finalSummary.entrySet().stream()
                    .map(e -> "wid=" + e.getKey() + ", state=" + e.getValue().get("state")
                            + ", gatewayState=" + e.getValue().get("gatewayState")
                            + ", transId=" + e.getValue().get("transId"))
                    .collect(Collectors.joining(" | "));

            LOG.info("[FINAL] Итоговая сводка после поллинга (2 мин интервал): {}", summaryStr);

            OperationUserResponseDTO out = new OperationUserResponseDTO();
            out.setGroupId(0L);
            out.setTransId(firstRes.getTransId() + "," + String.join(",", w2Trans.values()));
            out.setTotalWires(total);
            out.setApproved(1 + approved);
            out.setDeleted(deleted);
            out.setFailed(failed + (totalTail  - approved - deleted - timeout));
            out.setStatusSummary(String.format("APPROVED=%d, DELETED=%d, TIMEOUT=%d", approved, deleted, timeout));

            String statusSummary = String.format("APPROVED=%d, DELETED=%d, TIMEOUT=%d, FAILED=%d",
                    out.getApproved(), out.getDeleted(), out.getTimeout(), out.getFailed());
            out.setStatusSummary(statusSummary);

            if (out.getApproved() >= 1) {
                out.setErrorCode("0");
                out.setErrorNote("Успешно. " + statusSummary);
            } else {
                out.setErrorCode("FAILED");
                out.setErrorNote("Критическая ошибка. " + statusSummary);
            }
            return out;

        } catch (NumberFormatException e) {
            LOG.error("[BUY] Неверный TEMPLATE_ID: {}", e.getMessage());
            return buildErr("INVALID_TEMPLATE_ID", "Неверный TEMPLATE_ID: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("[BUY] Непредвиденная ошибка: {}", e.getMessage(), e);
            return buildErr("UNEXPECTED_ERROR", e.getMessage());
        }
    }

    private BigDecimal getCbuUsdRateOrThrow() {
        CurrencyRateResponseDTO[] rates = cbuRateService.getLatestRateUpdate();
        if (rates == null || rates.length == 0) throw new IllegalArgumentException("CBU rates пусты");
        for (CurrencyRateResponseDTO r : rates) {
            if (r == null) continue;
            boolean isUsd = "840".equals(r.getCode()) || "USD".equalsIgnoreCase(r.getCcy());
            if (isUsd) {
                String rateStr = r.getRate();
                if (rateStr == null || rateStr.isBlank()) throw new IllegalArgumentException("CBU USD rate пустой");
                return new BigDecimal(rateStr.trim());
            }
        }
        throw new IllegalArgumentException("USD (Code=840) не найден в CBU rates");
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
