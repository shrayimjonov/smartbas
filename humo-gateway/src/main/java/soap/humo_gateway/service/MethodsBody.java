package soap.humo_gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Component;
import soap.humo_gateway.dto.property.HumoCredentials;
import soap.humo_gateway.dto.enums.SubSystem;
import soap.humo_gateway.dto.request.*;
import soap.humo_gateway.exception.CardTypeInCorrect;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MethodsBody {
    private final BinsService binsService;

    private final HumoCredentials humoCredentials;

    private static final String login = "";

    private String sessionId(SubSystem subSystem) {
        return subSystem.toString() + "_" + LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond() + LocalDateTime.now().getNano() + "d";
    }

    private String centreId(String card) {
        return binsService.findAllBanks()
                .stream()
                .filter(z -> z.getBinCode().equals(card.substring(0, 8)))
                .findFirst()
                .orElseThrow(() -> new CardTypeInCorrect(String.format("Centre id for %s pan not found", card)))
                .getCentreId();
    }

    private String getSwitchingId(String pan1, String pan2) {

        var davrBins = binsService.findDavrBankBinsNotVisa();
        var coBadgeBins = binsService.findCoBadgeBins();
        var pan1Bin = pan1.substring(0, 8);
        var pan2Bin = pan2.substring(0, 8);

        if (davrBins.stream().anyMatch(z -> z.getBinCode().equals(pan1Bin)) && davrBins.stream().anyMatch(z -> z.getBinCode().equals(pan2Bin))) {
            return "48";
        } else if (coBadgeBins.stream().anyMatch(b -> b.getBinCode().equals(pan1Bin)) || coBadgeBins.stream().anyMatch(b -> b.getBinCode().equals(pan2Bin))) {
            return "77";
        } else {
            return "50";
        }
    }

    public String history(HistoryRequest request) {

        var bankCode = binsService.findBankCodeByBin(request.getPan().substring(0, 8));
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<soapenv:Envelope\n" +
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <soapenv:Body>\n" +
                "        <queryTransactionHistory>\n" +
                "            <ConnectionInfo>\n" +
                "                <EXTERNAL_SESSION_ID>" + sessionId(request.getSubSystem()) + "\n" +
                "                </EXTERNAL_SESSION_ID>\n" +
                "                <BANK_C>" + bankCode + "</BANK_C>\n" +
                "                <GROUPC>01</GROUPC>\n" +
                "            </ConnectionInfo>\n" +
                "            <Parameters>\n" +
                "                <CARD>" + request.getPan() + "</CARD>\n" +
                "                <BEGIN_DATE>" + request.getBeginDate() + "T00:00:00</BEGIN_DATE>\n" +
                "                <END_DATE>" + request.getFinishDate() + "T23:59:59</END_DATE>\n" +
                "                <BANK_C>" + bankCode + "</BANK_C>\n" +
                "                <GROUPC>01</GROUPC>\n" +
                "                <LOCKING_FLAG>1</LOCKING_FLAG>\n" +
                "            </Parameters>\n" +
                "        </queryTransactionHistory>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String transitAccount(TransitRequest request) {
        var bankCode = request.getAccount().substring(0, 2);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<soapenv:Envelope\n" +
                "\txmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "\t<soapenv:Body>\n" +
                "\t\t<listAccounts>\n" +
                "\t\t\t<ConnectionInfo>\n" +
                "\t\t\t\t<EXTERNAL_SESSION_ID>" + sessionId(request.getSubSystem()) + "</EXTERNAL_SESSION_ID>\n" +
                "\t\t\t\t<BANK_C>" + bankCode + "</BANK_C>\n" +
                "\t\t\t\t<GROUPC>01</GROUPC>\n" +
                "\t\t\t</ConnectionInfo>\n" +
                "\t\t\t<Parameters>\n" +
                "\t\t\t\t<CARD_ACCT>" + request.getAccount() + "</CARD_ACCT>\n" +
                "\t\t\t</Parameters>\n" +
                "\t\t</listAccounts>\n" +
                "\t</soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String paymentById(PaymentRequest request) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAPENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ebppif1=\"urn:PaymentServer\">\n" +
                "<SOAP-ENV:Body>\n" +
                "<ebppif1:GetPayment>\n" +
                "<paymentID>" + request.getPaymentId() + "</paymentID>\n" +
                "<sessionID/>\n" +
                "<paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                "</ebppif1:GetPayment>\n" +
                "</SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
    }

    public String paymentByExternalId(RefNumRequest request) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAPENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ebppif1=\"urn:PaymentServer\">\n" +
                "<SOAP-ENV:Body>\n" +
                "<ebppif1:GetPayment>\n" +
                "<paymentRef>" + request.getRefNum() + "</paymentRef> \n" +
                "<sessionID/>\n" +
                "<paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                "</ebppif1:GetPayment>\n" +
                "</SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
    }


    public String returnFinishedById(ReturnRequest request) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:urn=\"urn:PaymentServer\">\n" +
                "<soapenv:Header/>\n" +
                "<soapenv:Body>\n" +
                "<urn:ReturnPayment>\n" +
                "<paymentID>" + request.getPaymentId() + "</paymentID>\n" +
                "<item>\n" +
                "<name>merchant_id</name>\n" +
                "<value>" + request.getMerchantId() + "</value>\n" +
                "</item>\n" +
                "<item>\n" +
                "<name>centre_id</name>\n" +
                "<value>Davr</value>\n" +
                "</item>\n" +
                "<item>\n" +
                "<name>terminal_id</name>\n" +
                "<value>" + request.getTerminalId() + "</value>\n" +
                "</item>\n" +
                "<paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                "</urn:ReturnPayment>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String returnFinishedByRefNum(ReturnRefRequest request) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:urn=\"urn:PaymentServer\">\n" +
                "<soapenv:Header/>\n" +
                "<soapenv:Body>\n" +
                "<urn:ReturnPayment>\n" +
                "<paymentRef>" + request.getRefNumber() + "</paymentRef>\n" +
                "<item>\n" +
                "<name>merchant_id</name>\n" +
                "<value>" + request.getMerchantId() + "</value>\n" +
                "</item>\n" +
                "<item>\n" +
                "<name>centre_id</name>\n" +
                "<value>Davr</value>\n" +
                "</item>\n" +
                "<item>\n" +
                "<name>terminal_id</name>\n" +
                "<value>" + request.getTerminalId() + "</value>\n" +
                "</item>\n" +
                "<paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                "</urn:ReturnPayment>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String cancelActiveById(CancelRequest request) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:PaymentServer\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <urn:CancelRequest>\n" +
                "            <paymentID>" + request.getPaymentId() + "</paymentID>\n" +
                "            <paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                "        </urn:CancelRequest>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String cancelActiveByRefNum(CancelRefRequest request) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:urn=\"urn:PaymentServer\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <urn:CancelRequest>\n" +
                "            <paymentRef>" + request.getRefNumber() + "</paymentRef>\n" +
                "            <paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                "        </urn:CancelRequest>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String reconcileEPosCreate(RecoCreateRequest request) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAPENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ebppif1=\"urn:PaymentServer\">\n" +
                " <SOAP-ENV:Body>\n" +
                " <ebppif1:Payment>\n" +
                " <billerRef>SOAP_RECO</billerRef>\n" +
                " <payinstrRef>SOAP_RECO</payinstrRef>\n" +
                " <paymentRef>" + request.getRefNumber() + "</paymentRef>\n" +
                " <details>\n" +
                " <item>\n" +
                " <name>terminal_id</name>\n" +
                " <value>" + request.getTerminalId() + "</value>\n" +
                " </item>\n" +
                " </details>\n" +
                " <paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                " </ebppif1:Payment>\n" +
                " </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
    }

    public String reconcileEPosFinish(RecoFinishRequest request) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:ebppif1=\"urn:PaymentServer\">\n" +
                " <SOAP-ENV:Header/>\n" +
                " <SOAP-ENV:Body>\n" +
                " <ebppif1:Payment>\n" +
                " <payinstrRef>SOAP_RECO</payinstrRef>\n" +
                " <paymentID>" + request.getPaymentId() + "</paymentID>\n" +
                " <confirmed>true</confirmed>\n" +
                " <finished>true</finished>\n" +
                " <paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                " </ebppif1:Payment>\n" +
                " </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
    }

    public String p2pTransfer(P2pRequest request) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAPENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ebppif1=\"urn:PaymentServer\">\n" +
                " <SOAP-ENV:Body>\n" +
                " <ebppif1:Request SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                " <language>en</language>\n" +
                " <switchingID>\n" +
                " <value>" + getSwitchingId(request.getPan1(), request.getPan2()) + "</value>\n" +
                " </switchingID>\n" +
                " <autoSwitch>1</autoSwitch>\n" +
                " <paymentRef>" + request.getRefNum() + "</paymentRef>\n" +
                " <details>\n" +
                " <item>\n" +
                " <name>pan</name>\n" +
                " <value>" + request.getPan1() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>expiry</name>\n" +
                " <value>" + request.getExpiry1() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>pan2</name>\n" +
                " <value>" + request.getPan2() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>amount</name>\n" +
                " <value>" + request.getAmount() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>ccy_code</name>\n" +
                " <value>860</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>merchant_id</name>\n" +
                " <value>" + request.getMerchantId() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>terminal_id</name>\n" +
                " <value>" + request.getTerminalId() + "</value>\n" +
                " </item>\n" +
                " </details>\n" +
                " <paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                " </ebppif1:Request>\n" +
                " </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
    }

    public String debitCreate(DebitCreateRequest request) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAPENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ebppif1=\"urn:PaymentServer\">\n" +
                " <SOAP-ENV:Body>\n" +
                " <ebppif1:Payment>\n" +
                " <language>en</language>\n" +
                " <billerRef>SOAP_DMS</billerRef>\n" +
                " <payinstrRef>SOAP_DMS</payinstrRef>" +
                " <sessionID>" + request.getRefNum() + "</sessionID>\n" +
                " <paymentRef>" + request.getRefNum() + "</paymentRef>\n" +
                " <details>\n" +
                " <item>\n" +
                " <name>pan</name>\n" +
                " <value>" + request.getPan() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>expiry</name>\n" +
                " <value>" + request.getExpiry() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>ccy_code</name>\n" +
                " <value>860</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>amount</name>\n" +
                " <value>" + request.getAmount() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>merchant_id</name>\n" +
                " <value>" + request.getMerchantId() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>terminal_id</name>\n" +
                " <value>" + request.getTerminalId() + "</value>\n" +
                " </item>\n" +
//                " <item>\n" +
//                " <name>point_code</name>\n" +
//                " <value>" + humoCredentials.getPointCode() + "</value>\n" +
//                " </item>\n" +
                " <item>\n" +
                " <name>centre_id</name>\n" +
                " <value>" + centreId(request.getPan()) + "</value>\n" +
                " </item>\n" +
                " </details>\n" +
                " <paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                " </ebppif1:Payment>\n" +
                " </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>\n";
    }


    public String debitConfirm(DebitConfirmRequest request) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAPENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ebppif1=\"urn:PaymentServer\">\n" +
                " <SOAP-ENV:Body>\n" +
                " <ebppif1:Payment>\n" +
                " <paymentID>" + request.getPaymentId() + "</paymentID>\n" +
                " <confirmed>true</confirmed>\n" +
                " <finished>true</finished>\n" +
                " <paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                " </ebppif1:Payment>\n" +
                " </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
    }

    public String credit(CreditRequest request) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:SOAPENC=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ebppif1=\"urn:PaymentServer\">\n" +
                " <SOAP-ENV:Body>\n" +
                " <ebppif1:Payment>\n" +
                " <billerRef>SOAP_TOCARD_2</billerRef>\n" +
                " <payinstrRef>SOAP_TOCARD_2</payinstrRef>\n" +
                " <sessionID>" + request.getRefNum() + "</sessionID>\n" +
                " <paymentRef>" + request.getRefNum() + "</paymentRef>\n" +
                " <details>\n" +
                " <item>\n" +
                " <name>pan2</name>\n" +
                " <value>" + request.getPan2() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>expiry</name>\n" +
                " <value>" + request.getExpiry() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>ccy_code</name>\n" +
                " <value>860</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>amount</name>\n" +
                " <value>" + request.getAmount() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>merchant_id</name>\n" +
                " <value>" + request.getMerchantId() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>terminal_id</name>\n" +
                " <value>" + request.getTerminalId() + "</value>\n" +
                " </item>\n" +
//                " <item>\n" +
//                " <name>point_code</name>\n" +
//                " <value>" + humoCredentials.getPointCode() + "</value>\n" +
//                " </item>\n" +
                " <item>\n" +
                " <name>centre_id</name>\n" +
                " <value>" + centreId(request.getPan2()) + "</value>\n" +
                " </item>\n" +
                " </details>\n" +
                " <paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                " </ebppif1:Payment>\n" +
                " </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>\n";
    }

    public String creditWithPassData(CreditRequest request) {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:SOAPENC=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ebppif1=\"urn:PaymentServer\">\n" +
                " <SOAP-ENV:Body>\n" +
                " <ebppif1:Payment>\n" +
                " <billerRef>SOAP_TOCARD_2</billerRef>\n" +
                " <payinstrRef>SOAP_TOCARD_2</payinstrRef>\n" +
                " <sessionID>" + request.getRefNum() + "</sessionID>\n" +
                " <paymentRef>" + request.getRefNum() + "</paymentRef>\n" +
                " <details>\n" +
                " <item>\n" +
                " <name>pan2</name>\n" +
                " <value>" + request.getPan2() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>expiry</name>\n" +
                " <value>" + request.getExpiry() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>ccy_code</name>\n" +
                " <value>860</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>amount</name>\n" +
                " <value>" + request.getAmount() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>merchant_id</name>\n" +
                " <value>" + request.getMerchantId() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>terminal_id</name>\n" +
                " <value>" + request.getTerminalId() + "</value>\n" +
                " </item>\n" +
//                " <item>\n" +
//                " <name>point_code</name>\n" +
//                " <value>" + humoCredentials.getPointCode() + "</value>\n" +
//                " </item>\n" +
                " <item>\n" +
                " <name>centre_id</name>\n" +
                " <value>" + centreId(request.getPan2()) + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_nationality</name>\n" +
                " <value>" + request.getParams().getSenderNationality() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_doc_type</name>\n" +
                " <value>" + request.getParams().getSenderDocType() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_serial_no</name>\n" +
                " <value>" + request.getParams().getSenderSerialNo() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_id_card</name>\n" +
                " <value>" + request.getParams().getSenderIdCard() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_doc_validthrough</name>\n" +
                " <value>" + request.getParams().getSenderDocValidThrough() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_person_code</name>\n" +
                " <value>" + request.getParams().getSenderPersonCode() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_surname</name>\n" +
                " <value>" + request.getParams().getSenderSurname() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_first_name</name>\n" +
                " <value>" + request.getParams().getSenderFirstName() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_middle_name</name>\n" +
                " <value>" + request.getParams().getSenderMiddleName() + "</value>\n" +
                " </item>\n" +
                " <item>\n" +
                " <name>sender_birth_date</name>\n" +
                " <value>" + request.getParams().getSenderBirthDate() + "</value>\n" +
                " </item>\n" +
                " </details>\n" +
                " <paymentOriginator>" + request.getLogin() + "</paymentOriginator>\n" +
                " </ebppif1:Payment>\n" +
                " </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>\n";
    }

    public String executeTransaction(ExecuteTransactionRequest request) {
        var externalSessionId = request.getExternalSessionId() == null || request.getExternalSessionId().trim().isEmpty()
                ? sessionId(request.getSubSystem())
                : request.getExternalSessionId();

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <soapenv:Body>\n" +
                "        <executeTransaction>\n" +
                "            <ConnectionInfo>\n" +
                "                <BANK_C>" + xmlValue(request.getBankC()) + "</BANK_C>\n" +
                "                <GROUPC>" + xmlValue(request.getGroupc()) + "</GROUPC>\n" +
                "                <EXTERNAL_SESSION_ID>" + xmlValue(externalSessionId) + "</EXTERNAL_SESSION_ID>\n" +
                "            </ConnectionInfo>\n" +
                "            <Parameters>\n" +
                "                <PAYMENT_MODE>" + request.getPaymentMode() + "</PAYMENT_MODE>\n" +
                "                <CARD>" + xmlValue(request.getCard()) + "</CARD>\n" +
//                "                <ACCOUNT_NO>" + xmlValue(request.getAccountNo()) + "</ACCOUNT_NO>\n" +
                "                <TRAN_TYPE>" + xmlValue(request.getTranType()) + "</TRAN_TYPE>\n" +
                "                <TRAN_CCY>" + xmlValue(request.getTranCcy()) + "</TRAN_CCY>\n" +
                "                <TRAN_AMNT>" + request.getTranAmnt() + "</TRAN_AMNT>\n" +
                "                <BANK_C>" + xmlValue(request.getBankC()) + "</BANK_C>\n" +
                "                <GROUPC>" + xmlValue(request.getGroupc()) + "</GROUPC>\n" +
                "                <DEAL_DESC>" + xmlValue(request.getDealDesc()) + "</DEAL_DESC>\n" +
                "            </Parameters>\n" +
                "        </executeTransaction>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    private String xmlValue(Object value) {
        if (value == null) {
            return "";
        }

        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    public String assignPin(PinAssignRequest request, String encryptedPin) {
        return "<SOAP-ENV:Envelope\n" +
                " xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                " xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                " xmlns:cs=\"urn:CardServices\">\n" +
                "    <SOAP-ENV:Body>\n" +
                "        <cs:assignPin>\n" +
                "            <primaryAccountNumber>" + request.getCard() + "</primaryAccountNumber>\n" +
                "            <originatorId>" + login + "</originatorId>\n" +
                "            <expirationDate>" + request.getExpiry() + "</expirationDate>\n" +
                "            <encryptedPin>" + encryptedPin + "</encryptedPin>\n" +
                "            <cardSecurityCode>" + request.getSecurityCode() + "</cardSecurityCode>\n" +
                "            <terminalId>" + humoCredentials.getPosTerminal() + "</terminalId>\n" +
                "        </cs:assignPin>\n" +
                "    </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
    }

    public String getBalance(BalanceRequest request) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:IIACardServices\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <urn:getCardAccountsBalance>\n" +
                "            <primaryAccountNumber>" + request.getCard() + "</primaryAccountNumber>\n" +
                "        </urn:getCardAccountsBalance>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String deActivateCard(DeactivateRequest request) {
        var davrBins = binsService.findDavrBankBins();

        if (davrBins.stream().noneMatch(z -> z.getBinCode().equals(request.getCard().substring(0, 8))))
            throw new CardTypeInCorrect(String.format("Card: %s is not Davr Bank card!", request.getCard()));

        return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:bin=\"urn:IssuingWS/binding\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <deactivateCard>\n" +
                "            <ConnectionInfo>\n" +
                "                <BANK_C>25</BANK_C>\n" +
                "                <GROUPC>01</GROUPC>\n" +
                "                <EXTERNAL_SESSION_ID>" + sessionId(request.getSubSystem()) + "</EXTERNAL_SESSION_ID>\n" +
                "            </ConnectionInfo>\n" +
                "            <Parameters>\n" +
                "                <CARD>" + request.getCard() + "</CARD>\n" +
                "            </Parameters>\n" +
                "        </deactivateCard>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String activateCard(DeactivateRequest request) {
        var davrBins = binsService.findDavrBankBins();

        if (davrBins.stream().noneMatch(z -> z.getBinCode().equals(request.getCard().substring(0, 8))))
            throw new CardTypeInCorrect(String.format("Card: %s is not Davr Bank card!", request.getCard()));

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<soapenv:Envelope\n" +
                " xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <soapenv:Body>\n" +
                "        <activateCard>\n" +
                "            <ConnectionInfo>\n" +
                "                <BANK_C>25</BANK_C>\n" +
                "                <GROUPC>01</GROUPC>\n" +
                "                <EXTERNAL_SESSION_ID>" + sessionId(request.getSubSystem()) + "</EXTERNAL_SESSION_ID>\n" +
                "            </ConnectionInfo>\n" +
                "            <Parameters>\n" +
                "                <CARD>" + request.getCard() + "</CARD>\n" +
                "                <BANK_C>25</BANK_C>\n" +
                "                <GROUPC>01</GROUPC>\n" +
                "            </Parameters>\n" +
                "        </activateCard>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String getPinCounter(PinCountReq request) {
        var bankCode = binsService.findBankCodeByBin(request.getCard().substring(0, 8));

        return "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'\n" +
                "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n" +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema'>\n" +
                "    <soapenv:Body>\n" +
                "        <GetCardPinTryCounter>\n" +
                "            <ConnectionInfo>\n" +
                "                <BANK_C>" + bankCode + "</BANK_C>\n" +
                "                <GROUPC>01</GROUPC>\n" +
                "                <EXTERNAL_SESSION_ID>" + sessionId(request.getSubSystem()) + "</EXTERNAL_SESSION_ID>\n" +
                "            </ConnectionInfo>\n" +
                "            <Parameters>\n" +
                "                <CARD>" + request.getCard() + "</CARD>\n" +
                "            </Parameters>\n" +
                "        </GetCardPinTryCounter>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String resetPinCounter(ResetPinReq request) {

        var davrBins = binsService.findDavrBankBins();

        if (davrBins.stream().noneMatch(z -> z.getBinCode().equals(request.getCard().substring(0, 8))))
            throw new CardTypeInCorrect(String.format("Card: %s is not Davr Bank card!", request.getCard()));

        return "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'\n" +
                "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n" +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema'>\n" +
                "    <soapenv:Body>\n" +
                "        <resetPINCounter>\n" +
                "            <ConnectionInfo>\n" +
                "                <BANK_C>25</BANK_C>\n" +
                "                <GROUPC>01</GROUPC>\n" +
                "                  <EXTERNAL_SESSION_ID>" + sessionId(request.getSubSystem()) + "</EXTERNAL_SESSION_ID>\n" +
                "            </ConnectionInfo>\n" +
                "            <Parameters>\n" +
                "                <CARD>" + request.getCard() + "</CARD>\n" +
                "                <EXPIRY>" + request.getExpiry() + "</EXPIRY>\n" +
                "                <TEXT>" + request.getText() + "</TEXT>\n" +
                "                <OFFLINE>1</OFFLINE>\n" +
                "            </Parameters>\n" +
                "        </resetPINCounter>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String transferDataToFo(TransferDataRequest request) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<soapenv:Envelope \n" +
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <soapenv:Body>\n" +
                "        <transferDataToFo>\n" +
                "            <ConnectionInfo>\n" +
                "            <EXTERNAL_SESSION_ID>" + sessionId(request.getSubSystem()) + "</EXTERNAL_SESSION_ID>\n" +
                "                <BANK_C>25</BANK_C>\n" +
                "                <GROUPC>01</GROUPC>\n" +
                "            </ConnectionInfo>\n" +
                "            <Parameters>\n" +
                "                <CARD>" + request.getCard() + "</CARD>\n" +
                "            </Parameters>\n" +
                "        </transferDataToFo>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }


}
