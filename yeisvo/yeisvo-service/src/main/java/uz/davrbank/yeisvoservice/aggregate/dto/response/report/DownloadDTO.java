package uz.davrbank.yeisvoservice.aggregate.dto.response.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadDTO {

    private String base64;
    private String fileName;
}
