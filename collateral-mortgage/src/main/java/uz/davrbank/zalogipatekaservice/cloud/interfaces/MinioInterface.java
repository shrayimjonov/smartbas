package uz.davrbank.zalogipatekaservice.cloud.interfaces;


public interface MinioInterface {

    /**
     * Create and upload an Excel file to MinIO with all necessary parameters.
     *
     * @param startDate    start date filter
     * @param endDate      end date filter
     * @param cadastreType cadastre type filter
     * @param state        state filter
     * @param branch       branch filter
     * @param loanId       loan ID filter
     * @param clientId     client ID filter
     * @param pinfl        client PINFL filter
     * @param clientName   client name filter
     * @param cadastreNum  cadastre number filter
     * @param bytes        Excel file content
     * @return the object path in MinIO
     */
    String createExcelFileInMinio(String startDate,
                                  String endDate,
                                  Integer cadastreType,
                                  Integer state,
                                  String branch,
                                  Integer loanId,
                                  String clientId,
                                  String pinfl,
                                  String clientName,
                                  String cadastreNum,
                                  byte[] bytes);

    //byte[] downloadFileFromMinio(String objectName);
}
