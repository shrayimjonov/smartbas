package uz.davrbank.zalogipatekaservice.service.cases;


import uz.davrbank.zalogipatekaservice.aggregate.dto.request.ZalogIpotekaRequestParam;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponsePaginationDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseSortDTO;

public interface ZalogIpotekaService {

    ResponseDTO getAll(ZalogIpotekaRequestParam requestParam);

    ResponsePaginationDTO getPageable(ZalogIpotekaRequestParam requestParam);

    ResponseSortDTO getPageableSorted(ZalogIpotekaRequestParam requestParam);
}
