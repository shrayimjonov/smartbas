package davr.uzcard.Model.Request;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardHistory {
    public String jsonrpc;
    public String method;
    public String id;
    public Params params;

    @Data
    @Builder
    public static class Params {
        public Criteria criteria;
    }

    @Data
    @Builder
    public static class Criteria {
        public List<String> cardIds;
        public Range range;
        public int pageNumber;
        public int pageSize;
    }

    @Data
    @Builder
    public static class Range {
        public String startDate;
        public String endDate;
    }
}
