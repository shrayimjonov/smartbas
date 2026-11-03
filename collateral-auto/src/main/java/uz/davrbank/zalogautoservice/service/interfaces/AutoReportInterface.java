package uz.davrbank.zalogautoservice.service.interfaces;

import org.springframework.http.ResponseEntity;

public interface AutoReportInterface {
    ResponseEntity<?> uploadReportToMinio(String startDate,
                                          String endDate,
                                          Integer state,
                                          String branch,
                                          Integer loanId,
                                          String clientId,
                                          String pinfl,
                                          String clientName,
                                          String stateNumber,
                                          String address,
                                          String username);

}