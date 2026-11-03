package uz.davrbank.yeisvoservice.aggregate.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportAllRequest {
    private TransType transType;
    private String startDate;
    private String endDate;
}
