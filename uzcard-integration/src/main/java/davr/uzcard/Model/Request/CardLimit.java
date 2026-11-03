package davr.uzcard.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardLimit {
    @Builder.Default
    private String  jsonrpc = "2.0";
    @Builder.Default
    public String id = UUID.randomUUID().toString();
    @Builder.Default
    public String method = "card.limit.list";
}
