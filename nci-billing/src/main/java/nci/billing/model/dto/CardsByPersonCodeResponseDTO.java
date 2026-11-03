package nci.billing.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.response.NCICardsByPersonCodeResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardsByPersonCodeResponseDTO {
    private String branch;
    private String client;
    private String pan;
    private String expiry;
    private String holder;


    public CardsByPersonCodeResponseDTO(NCICardsByPersonCodeResponse response) {
        this.branch = response.getBranch();
        this.client = response.getClient();
        this.pan = response.getPan();
        this.expiry = response.getExpiry();
        this.holder = response.getHolder();
    }
}
