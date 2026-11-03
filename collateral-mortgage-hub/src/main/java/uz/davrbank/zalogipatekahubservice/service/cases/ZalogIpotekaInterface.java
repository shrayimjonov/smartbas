package uz.davrbank.zalogipatekahubservice.service.cases;

import uz.davrbank.zalogipatekahubservice.aggregate.dto.response.ResponseDTO;
import uz.davrbank.zalogipatekahubservice.aggregate.dto.response.ResponsePaginationDTO;
import uz.davrbank.zalogipatekahubservice.aggregate.dto.response.ResponseSortDTO;

public interface ZalogIpotekaInterface {
    ResponseDTO getAllNoPagination(int iCadastreType, int endDate);

    ResponsePaginationDTO getAllPagination(Integer cadastreType, Integer state, Integer pageNumber,
                                           Integer pageSize);

    ResponseSortDTO getAllPaginationSort(Integer cadastreType, Integer state, Integer pageNumber,
                                         Integer pageSize, String sort);
}