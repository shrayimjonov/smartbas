package uz.davrbank.userinfobase.dto.request.files;


public record FileUploadRequest(
        String loanExtId,
        String file
) {}