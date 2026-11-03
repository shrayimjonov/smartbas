package uz.davrbank.clientaccounts.aggregate.cloud.service;

public interface ClientAccountMinioInterface {
    String uploadExcelToMinio(byte[] excelBytes, String iDate, String iBranch, String iAccList);
}