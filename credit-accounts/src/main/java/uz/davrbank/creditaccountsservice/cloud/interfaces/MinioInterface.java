package uz.davrbank.creditaccountsservice.cloud.interfaces;


import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;

public interface MinioInterface {

    String createExcelFileInMinio(CreditAccountRequestDTO creditAccountRequestDTO,
                                  byte[] bytes);

}
