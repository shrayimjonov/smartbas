package uz.davrbank.bustreasury.aggregate.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocTypeRequestDTO {
    @JsonProperty("KOD_DOC")
    private String kodDoc;
    @JsonProperty("NAIM_DOC")
    private String naimDoc;
    @JsonProperty("DATE_OPEN")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOpen;
    @JsonProperty("DATE_CLOSE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateClose;
    @JsonProperty("ACT")
    private String act;
}
