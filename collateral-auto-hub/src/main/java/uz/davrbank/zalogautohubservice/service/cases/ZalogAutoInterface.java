package uz.davrbank.zalogautohubservice.service.cases;

import uz.davrbank.zalogautohubservice.aggregate.DTO.response.ResponseZalogAutoHubDTO;

public interface ZalogAutoInterface {
    ResponseZalogAutoHubDTO getAllNoPagination(String startDate, String endDate, Integer State);
    ResponseZalogAutoHubDTO getAllPagination(String startDate, String endDate, Integer State, Integer pageNumber, Integer pageSize);
    ResponseZalogAutoHubDTO getAllPaginationSort(String startDate, String endDate, Integer State, Integer pageNumber, Integer pageSize, String sort);
}