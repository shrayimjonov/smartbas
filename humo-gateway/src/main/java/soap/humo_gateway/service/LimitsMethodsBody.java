package soap.humo_gateway.service;

import org.springframework.stereotype.Service;
import soap.humo_gateway.dto.request.limits.GetCardLimitBalanceRequest;
import soap.humo_gateway.dto.request.limits.GetCardLimitRequest;
import soap.humo_gateway.dto.request.limits.SetCardLimitRequest;

@Service
public class LimitsMethodsBody {

    public String getLimitsRefData() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:urn=\"urn:Limits\">\n" +
                " <soapenv:Header/>\n" +
                " <soapenv:Body>\n" +
                " <urn:getLimitsRefData>\n" +
                " <code_owner>CMS</code_owner>\n" +
                " <ref_type>LIMIT_TYPE</ref_type>\n" +
                " </urn:getLimitsRefData>\n" +
                " </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String getCardLimits(GetCardLimitRequest request) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:urn=\"urn:Limits\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <urn:getLimits>\n" +
                "            <limit_event>\n" +
                "                <key_type>CARD</key_type>\n" +
                "                <key_value>" + request.getCard() + "</key_value>\n" +
                "                <code_owner>CMS</code_owner>\n" +
                "            </limit_event>\n" +
                "        </urn:getLimits>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String setLimitBalance(SetCardLimitRequest request, String limits) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:urn=\"urn:Limits\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <urn:setLimits>\n" +
                "            <limit_event>\n" +
                "                <key_type>CARD</key_type>\n" +
                "                <key_value>" + request.getCard() + "</key_value>\n" +
                "                <code_owner>CMS</code_owner>\n" +
                limits +
                "            </limit_event>\n" +
                "        </urn:setLimits>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String deleteLimitBalance(SetCardLimitRequest request) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:urn=\"urn:Limits\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <urn:setLimits>\n" +
                "            <limit_event>\n" +
                "                <key_type>CARD</key_type>\n" +
                "                <key_value>" + request.getCard() + "</key_value>\n" +
                "                <code_owner>CMS</code_owner>\n" +
                "            </limit_event>\n" +
                "        </urn:setLimits>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public String getCardLimitBalance(GetCardLimitBalanceRequest request) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:urn=\"urn:Limits\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <urn:getLimitsBalance>\n" +
                "            <limit_balance_event>\n" +
                "                <code_owner>CMS</code_owner>\n" +
                "                <limit_type>" + request.getType() + "</limit_type>\n" +
                "                <limit_key_type>CARD</limit_key_type>\n" +
                "                <limit_key_value>" + request.getCard() + "</limit_key_value>\n" +
                "            </limit_balance_event>\n" +
                "        </urn:getLimitsBalance>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }
}
