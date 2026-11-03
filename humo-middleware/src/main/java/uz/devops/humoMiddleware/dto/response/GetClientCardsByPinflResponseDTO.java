package uz.devops.humoMiddleware.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetClientCardsByPinflResponseDTO {
    private List<String> data;
}
