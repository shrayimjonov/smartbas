package uz.davrbank.zalogautoservice.aggregate.cloud.service;

public interface AutoMinioInterface {
    String createExcelFileInMinio(
            String startDate, String endDate,
            Integer state,
            String branch, Integer loanId,
            String clientId, String pinfl,
            String clientName, String stateNumber, String address,
            byte[] bytes
    );

    String createExcelFileInMinio(
            String startDate, String endDate,
            Integer state,
            String branch, Integer loanId,
            String clientId, String pinfl,
            String clientName, String stateNumber, String address,
            byte[] bytes,
            String fileName
    );
}