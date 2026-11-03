package humo.integration.card.Model.Request;

import humo.integration.card.Model.Dto.HumoHistoryRequestDTO;
import humo.integration.card.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryMiddlewareRequest {
    private String id;
    private HistoryParams params;

    public HistoryMiddlewareRequest(HumoHistoryRequestDTO requestDTO) {
        this.id = "dv_hi_" + System.currentTimeMillis();
        this.params = new HistoryParams(
                requestDTO
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HistoryParams {
        private String pan;
        private String dateFrom;
        private String dateTo;

        public HistoryParams(HumoHistoryRequestDTO requestDTO) {
            this.pan = requestDTO.getCard();
            this.dateFrom = DateUtils.stringToDate(requestDTO.getDateFrom(), "dd.MM.yyyy");
            this.dateTo = DateUtils.stringToDateV2(requestDTO.getDateTo(), "dd.MM.yyyy");
        }
    }
}
