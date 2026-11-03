package humo.integration.card.Visa.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardRequest {
    private String request_id;
    private List<String> card_ids;
    private String card_type;
}
