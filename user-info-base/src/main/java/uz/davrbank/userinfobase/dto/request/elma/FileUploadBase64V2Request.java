package uz.davrbank.userinfobase.dto.request.elma;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FileUploadBase64V2Request {
    private String loanExtId;
    private Integer remainingLoans;
    private List<String> files;
    private String branchId;
}
