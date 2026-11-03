package davr.uzcard.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransPayDto {
    private Long amount;
    private String cardId;
    private String ext;
    private String merchant;
    private String terminalId;
    private String stan;
    private int port;
}
