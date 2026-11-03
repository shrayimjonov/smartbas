package uz.davrbank.userinfobase.dto.request.elma;


import lombok.Data;

@Data
public class FileUploadBase64Request {
    private String loanExtId;
    private Integer remainingLoans;
    private String file;
}
