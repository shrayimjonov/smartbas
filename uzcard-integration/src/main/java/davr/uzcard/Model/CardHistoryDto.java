package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardHistoryDto {
    private String token;
    private String startDate;
    private String endDate;
    private int pageNumber;
    private int pageSize;
}
