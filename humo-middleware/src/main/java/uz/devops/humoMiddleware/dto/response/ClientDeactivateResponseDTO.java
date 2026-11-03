package uz.devops.humoMiddleware.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDeactivateResponseDTO {
    private String state;
    private String message;
}
