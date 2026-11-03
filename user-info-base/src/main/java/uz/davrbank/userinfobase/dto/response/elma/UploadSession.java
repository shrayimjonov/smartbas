package uz.davrbank.userinfobase.dto.response.elma;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadSession {
    private int remainingLoans;
    private List<FileUploadResponse> uploadedFiles = new ArrayList<>();
}
