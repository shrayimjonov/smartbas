package nci.billing.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import nci.billing.exception.TimeOut;
import nci.billing.exception.UnknownType;
import nci.billing.feign.BackAPIFeign;
import nci.billing.feign.IsAPIFeign;
import nci.billing.feign.IsLoanFeign;
import nci.billing.model.dto.*;
import nci.billing.model.enums.PayStatus;
import nci.billing.model.enums.PaymentType;
import nci.billing.model.enums.ResponseCode;
import nci.billing.model.enums.SystemType;
import nci.billing.model.property.IsLoanProps;
import nci.billing.model.request.CheckTransactionRequest;
import nci.billing.model.request.PayRequest;
import nci.billing.model.request.RepayMainDebtRequest;
import nci.billing.model.request.RepayMainDebtV2Request;
import nci.billing.model.response.BaseIsAPIResponse;
import nci.billing.model.response.CheckResponse;
import nci.billing.model.response.LoanResponse;
import nci.billing.service.CoreService;
import nci.billing.service.InfoService;
import nci.billing.service.ReverseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CoreImpl implements CoreService {
    private final IsLoanProps properties;
    private final IsLoanFeign isLoanFeign;
    private final InfoService infoService;
    private final IsAPIFeign isAPIFeign;
    private final ReverseService reverseService;
    private final FraudDetection fraudDetection;
    private final BackAPIFeign backAPIFeign;
    private final static String FX_DEAL = "31"; // select * from ss_deal_fxdoc
    private final static String DEAL = "5"; // select * from bank981.ss_course_type


    public CoreImpl(IsLoanProps properties,
                    IsLoanFeign isLoanFeign,
                    @Qualifier("infoImpl") InfoService infoService,
                    IsAPIFeign isAPIFeign, ReverseService reverseService, FraudDetection fraudDetection, BackAPIFeign backAPIFeign) {
        this.properties = properties;
        this.isLoanFeign = isLoanFeign;
        this.infoService = infoService;
        this.isAPIFeign = isAPIFeign;
        this.reverseService = reverseService;
        this.fraudDetection = fraudDetection;
        this.backAPIFeign = backAPIFeign;
    }

    @Override
    public CheckResponse checkTransaction(CheckPaymentDto request) {
        var type = returnSubSystemCredentials(request.getSystem());

        try {
            var req = CheckTransactionRequest.builder()
                    .requestId(request.getRequestId())
                    .login(type.get("login"))
                    .password(type.get("password"))
                    .build();
            log.info("sending request to checkTransaction method, request: {}", req);
            var response = isLoanFeign.getClientDataByLoanId(req);
            log.info("checkTransaction method response: {}", response);
            return response;
        } catch (Exception ex) {
            log.error("checkTransaction method error: {}", ex.getMessage());
            throw new TimeOut("isLoan checkTransaction method server throws error: %s".formatted(ex.getMessage()));
        }
    }

    private Map<String, String> returnSubSystemCredentials(SystemType type) {
        Map<String, String> credentials = new HashMap<>();
        if (type.equals(SystemType.PAYMO)) {
            credentials.put("login", properties.getPaymo().getUsername());
            credentials.put("password", properties.getPaymo().getPassword());
            return credentials;
        } else if (type.equals(SystemType.GENESIS)) {
            credentials.put("login", properties.getGenesis().getUsername());
            credentials.put("password", properties.getGenesis().getPassword());
            return credentials;
        } else {
            log.error("%s type is unknown system type".formatted(type));
            throw new UnknownType("%s type is unknown system type".formatted(type));
        }

    }

    @Override
    public ProblemDto pay(PayDto dto) {
        log.info("""

                        Do Transaction method started...
                        loanId..........: {}
                        terminalId......: {}
                        paymentId.......: {}
                        amount..........: {}
                        paymentType.....: {}
                        systemType......: {}""",
                dto.getLoanId(),
                dto.getTerminalId(),
                dto.getPaymentId(),
                dto.getAmount(),
                dto.getPaymentType(),
                dto.getSystemType());


        if (!checkBankDayStatus(dto.getLoanId().substring(0, 5)))
            return ProblemDto.builder()
                    .code(ResponseCode.BANK_CLOSED.getCode())
                    .message(ResponseCode.BANK_CLOSED.getMessage())
                    .build();


        var request = PayRequest.builder()
                .loanId(dto.getLoanId())
                .interest(0L)
                .paymentSrc("2")
                .amount(dto.getAmount())
                .terminalId(dto.getTerminalId())
                .paymentId(dto.getPaymentId())
                .build();


        if (dto.getPaymentType().equals(PaymentType.UZCARD))
            request.setPaymentType(PaymentType.UZCARD.getPaymentType());
        else if (dto.getPaymentType().equals(PaymentType.HUMO))
            request.setPaymentType(PaymentType.HUMO.getPaymentType());
        else
            throw new UnknownType("Type %s is unknown".formatted(dto.getPaymentType()));

        var type = returnSubSystemCredentials(dto.getSystemType());


        request.setUsername(type.get("login"));
        request.setPassword(type.get("password"));


        LoanResponse loanInfo;

        try {
            loanInfo = infoService.getClientDebtInfoByLoanId(LoanDto.builder()
                    .loanId(request.getLoanId())
                    .build());
        } catch (Exception ex) {
            log.error("isLoan DO Transaction method server throws error: %s".formatted(ex.getMessage()));
            return ProblemDto.builder()
                    .code(ResponseCode.FAILED_CHECK.getCode())
                    .detail(ResponseCode.FAILED_CHECK.getMessage())
                    .message(ex.getMessage())
                    .build();
        }

        if (loanInfo.getError() != null)
            return ProblemDto.builder()
                    .code(ResponseCode.LOAN_NOT_FOUND.getCode())
                    .detail(ResponseCode.LOAN_NOT_FOUND.getMessage())
                    .message(loanInfo.getError().getMessage())
                    .build();


        request.setRequestId(loanInfo.getRequestId());

        return doTransaction(request);
    }

    private ProblemDto doTransaction(PayRequest request) {
        var fraud = fraudDetection.isFraudulentPayment(request);
        try {
            log.info("isLoan DO Transaction request: {}", request);
            var response = isLoanFeign.doTransaction(request);


            log.info("isLoan DO Transaction response: {}", response);

            return ProblemDto.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .params(Map.of("request_id", response.getRequest_id(),
                            "general_id", response.getGenId()))
                    .build();
        } catch (Exception ex) {

            log.error("IsLoan do transaction method exception request_id: {}", request.getRequestId());

            if (ex instanceof FeignException)
                log.error("isLoan Feign exception body: {}", ((FeignException) ex).contentUTF8());

            log.error("isLoan DO Transaction method server throws error: %s".formatted(ex.getMessage()));
            fraudDetection.updateFraudToFraudulentIsTrue(fraud.getId(), false, ex.getMessage());

            var response = ProblemDto.builder()
                    .code(ResponseCode.FAILED_PAY.getCode())
                    .message(ResponseCode.FAILED_PAY.getMessage())
                    .detail(ex.getMessage())
                    .params(Map.of("request_id", request.getRequestId()))
                    .build();
            log.warn("isLoan DO Transaction response after exception: {}", response);
            return response;
        }
    }

    private boolean checkBankDayStatus(String branch) {
        return infoService.getBankDayStatus(branch);
    }

    @Override
    public ProblemDto reverseNCITransaction(ReverseDto dto) {

        var params = Map.of("ACT", "501",
                "MFO", dto.getBranch(),
                "TRANSACTION_ID", dto.getGeneralId());
        log.info("reverse Transaction method request : {} ", params);
        var reverse = reverseService.create(params);
        try {
            var res = isAPIFeign.baseConnector(params);
            var response = new ObjectMapper().readValue(res, BaseIsAPIResponse.class);
            log.info("reverse Transaction method response : {} ", response);
            if (!response.getResult().equals("1")) {
                reverseService.update(reverse.getId(), response.toString(), PayStatus.FAILED);
                return ProblemDto.builder()
                        .code(ResponseCode.REVERSE_FAILED.getCode())
                        .detail(ResponseCode.REVERSE_FAILED.getMessage())
                        .message(response.getResultNote())
                        .build();
            }
            reverseService.update(reverse.getId(), response.toString(), PayStatus.SUCCESS);
            return ProblemDto.builder()
                    .code(ResponseCode.REVERSE_SUCCESS.getCode())
                    .detail(ResponseCode.REVERSE_SUCCESS.getMessage())
                    .message(response.getResultNote())
                    .params(Map.of("doc_id", response.docId))
                    .build();

        } catch (RetryableException r) {
            reverseService.update(reverse.getId(), r.getMessage(), PayStatus.FAILED);
            return ProblemDto.builder()
                    .code(ResponseCode.REVERSE_NOT_COMPLETED.getCode())
                    .detail(ResponseCode.REVERSE_NOT_COMPLETED.getMessage())
                    .message(r.getMessage())
                    .build();
        } catch (Exception ex) {
            reverseService.update(reverse.getId(), ex.getMessage(), PayStatus.FAILED);
            return ProblemDto.builder()
                    .code(ResponseCode.FAILED.getCode())
                    .detail(ResponseCode.FAILED.getMessage())
                    .message(ex.getMessage())
                    .build();
        }
    }


    @Override
    public ProblemDto forexOperation(ForexTranRequestDTO requestDTO) {
        var params = new HashMap<String, String>();
        params.put("ACT", "516");
        params.put("MFO", requestDTO.getBranch());
        params.put("ACC_CL", requestDTO.getAccountDebit());
        params.put("ACC_CO", requestDTO.getAccountCredit());
        params.put("SUMMA", String.valueOf(requestDTO.getAmount()));
        params.put("CURRENCY", requestDTO.getCurrency());
        params.put("FX_DEAL", FX_DEAL);
        params.put("DEAL", DEAL);
        params.put("ID", String.valueOf(requestDTO.getExtId()));
        params.put("PURPOSE", requestDTO.getPurpose());
        params.put("ACTION_ID", "19"); //Провести без ввода и утверждения || select * from action_general

        log.info("Forex operation method request : {} ", params);

        try {
            var res = isAPIFeign.baseConnector(params);
            var response = new ObjectMapper().readValue(res, BaseIsAPIResponse.class);
            log.info("Forex operation method response : {} ", response);
            if (!response.getResult().equals("1")) {
                return ProblemDto.builder()
                        .code(ResponseCode.FAILED.getCode())
                        .detail(ResponseCode.FAILED.getMessage())
                        .message(response.getResultNote())
                        .build();
            }

            return ProblemDto.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .detail(response.getDocId())
                    .message(response.getResultNote())
                    .build();

        } catch (Exception ex) {
            return ProblemDto.builder()
                    .code(ResponseCode.TRANSACTION_NOT_COMPLETED.getCode())
                    .detail(ResponseCode.TRANSACTION_NOT_COMPLETED.getMessage())
                    .message(ex.getMessage())
                    .build();
        }
    }


    @Override
    public ProblemDto repayMainDebtByLoan(RepayMainDebtDto dto) {
        var request = RepayMainDebtRequest.builder()
                .branch(dto.getLoanExternal().substring(0, 5))
                .id(dto.getLoanExternal().substring(5))
                .byMainAccount("1")
                .build();
        log.info("repay main debt by loan request: {}", request);

        try {
            var response = backAPIFeign.repayMainDebt(request);

            var result = Arrays.stream(response).toList().get(0);
            if (result.getRes().equals("1"))
                return ProblemDto.builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .detail(ResponseCode.SUCCESS.getMessage())
                        .message(result.getErr())
                        .build();
            else
                return ProblemDto.builder()
                        .code(ResponseCode.DEBT_REPAY_FAILED.getCode())
                        .detail(ResponseCode.DEBT_REPAY_FAILED.getMessage())
                        .message(result.getErr())
                        .build();
        } catch (Exception ex) {
            return ProblemDto.builder()
                    .code(ResponseCode.DEBT_REPAY_FAILED.getCode())
                    .detail(ResponseCode.DEBT_REPAY_FAILED.getMessage())
                    .message(ex.getMessage())
                    .build();
        }


    }

    @Override
    public ProblemDto repayMainDebtByLoanV2(RepayDTO dto) {
        var request = RepayMainDebtV2Request.builder()
                .branch(dto.getLoanExternal().substring(0, 5))
                .id(dto.getLoanExternal().substring(5))
                .amount(dto.getAmount().toString())
                .byMainAccount("1")
                .build();
        log.info("repay main debt by loan v2 request: {}", request);

        try {
            var response = backAPIFeign.repayMainDebtV2(request);

            var result = Arrays.stream(response).toList().get(0);
            if (result.getRes().equals("1"))
                return ProblemDto.builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .detail(ResponseCode.SUCCESS.getMessage())
                        .message(result.getErr())
                        .build();
            else
                return ProblemDto.builder()
                        .code(ResponseCode.DEBT_REPAY_FAILED.getCode())
                        .detail(ResponseCode.DEBT_REPAY_FAILED.getMessage())
                        .message(result.getErr())
                        .build();
        } catch (Exception ex) {
            return ProblemDto.builder()
                    .code(ResponseCode.DEBT_REPAY_FAILED.getCode())
                    .detail(ResponseCode.DEBT_REPAY_FAILED.getMessage())
                    .message(ex.getMessage())
                    .build();
        }


    }

    @Override
    public ProblemDto simpleTransaction(SimpleTranRequestDTO requestDTO) {
        var params = new HashMap<String, String>();
        params.put("ACT", "500");
        params.put("SESSION_ID", "1DEF34512");
        params.put("ACCOUNT_DT", requestDTO.getAccountDebit());
        params.put("MFO_DT", requestDTO.getBranchDebit());
        params.put("ACCOUNT_CR", requestDTO.getAccountCredit());
        params.put("MFO_CR", requestDTO.getBranchCredit());
        params.put("NAME_CR", "Kredit pogash fasto repay");
        params.put("SUMMA", String.valueOf(requestDTO.getAmount()));
        params.put("NAZ_PLA", requestDTO.getPurpose());
        params.put("CODE_NAZ_PLA", "00634");
        var docNum = requestDTO.getExtId() > 0 ? requestDTO.getExtId() : requestDTO.getExtId() * -1;
        params.put("NUM_DOC", String.valueOf(docNum));
        params.put("SIGN", "14444fb3887a4d0feb7570f40c73b805");
        params.put("INN_CR", "null");
        params.put("STATE", String.valueOf(3));
        params.put("TYPE_DOC", "01");
        params.put("TRANSACTION_ID", String.valueOf(requestDTO.getExtId()));
        params.put("ID_TRANSH_PURP", "00634");
        params.put("CASH_CODE", null);


        log.info("Simple transaction operation method request : {} ", params);

        try {
            var res = isAPIFeign.baseConnector(params);
            var response = new ObjectMapper().readValue(res, BaseIsAPIResponse.class);
            log.info("Forex operation method response : {} ", response);
            if (!response.getResult().equals("1")) {
                return ProblemDto.builder()
                        .code(ResponseCode.FAILED.getCode())
                        .detail(ResponseCode.FAILED.getMessage())
                        .message(response.getResultNote())
                        .build();
            }

            return ProblemDto.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .detail(response.getDocId())
                    .message(response.getResultNote())
                    .build();

        } catch (Exception ex) {
            return ProblemDto.builder()
                    .code(ResponseCode.TRANSACTION_NOT_COMPLETED.getCode())
                    .detail(ResponseCode.TRANSACTION_NOT_COMPLETED.getMessage())
                    .message(ex.getMessage())
                    .build();
        }
    }

}
