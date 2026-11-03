package humo.integration.card.Visa.Model.Convert;

import humo.integration.card.Visa.Model.CardResponse;
import humo.integration.card.Visa.Model.DataHistoryResp;
import lombok.var;
import org.springframework.stereotype.Service;

@Service
public class DataToCard {

    public DataHistoryResp dataHistoryToCardResponse(DataMob dataMob) {
        DataMob.Details lastRow;


        if (dataMob.getDetails().size() > 1) {
            lastRow = dataMob.getDetails().get(dataMob.getDetails().size()-1);
            for (DataMob.Details row : dataMob.getDetails()) {
                if (!row.getR_MOB_PHONE().equals("")) {
                    lastRow = row;
                }
            }
            return DataHistoryResp.builder()
                    .B_DATE(lastRow.getB_DATE())
                    .STATUS(lastRow.getSTATUS())
                    .R_E_MAILS(lastRow.getR_E_MAILS())
                    .CLIENT_B(lastRow.getCLIENT_B())
                    .R_MOB_PHONE(lastRow.getR_MOB_PHONE())
                    .R_STREET(lastRow.getR_STREET())
                    .CLIENT(lastRow.getCLIENT())
                    .build();
        } else {
            return DataHistoryResp.builder()
                    .B_DATE(dataMob.getDetails().get(0).getB_DATE())
                    .STATUS(dataMob.getDetails().get(0).getSTATUS())
                    .R_E_MAILS(dataMob.getDetails().get(0).getR_E_MAILS())
                    .CLIENT_B(dataMob.getDetails().get(0).getCLIENT_B())
                    .R_MOB_PHONE(dataMob.getDetails().get(0).getR_MOB_PHONE())
                    .R_STREET(dataMob.getDetails().get(0).getR_STREET())
                    .CLIENT(dataMob.getDetails().get(0).getCLIENT())
                    .build();
        }

    }
}
