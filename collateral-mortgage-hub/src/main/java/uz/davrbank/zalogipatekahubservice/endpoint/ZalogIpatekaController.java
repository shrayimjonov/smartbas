package uz.davrbank.zalogipatekahubservice.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.zalogipatekahubservice.aggregate.dto.response.ResponseDTO;
import uz.davrbank.zalogipatekahubservice.aggregate.dto.response.ResponsePaginationDTO;
import uz.davrbank.zalogipatekahubservice.aggregate.dto.response.ResponseSortDTO;
import uz.davrbank.zalogipatekahubservice.service.cases.ZalogIpotekaInterface;

@RestController
@RequestMapping("/v1/reports/zalog/ipateka")
public class ZalogIpatekaController {

    private final ZalogIpotekaInterface zalogIpotekaInterface;

    public ZalogIpatekaController(ZalogIpotekaInterface zalogIpotekaInterface) {
        this.zalogIpotekaInterface = zalogIpotekaInterface;
    }

    //---------------------------zalog ipoteka ---------------------------

    @PostMapping("/get/all")
    public ResponseEntity<ResponseDTO> getData(
            @RequestParam("cadastreType") Integer cadastreType,
            @RequestParam("state") Integer state
    ) {
        return ResponseEntity.ok(zalogIpotekaInterface.getAllNoPagination(cadastreType, state));
    }

    //---------------------------zalog ipoteka pagination --------------------
    @PostMapping("/page")
    public ResponseEntity<ResponsePaginationDTO> getPagination(
            @RequestParam("cadastreType") Integer cadastreType,
            @RequestParam("state") Integer state,
            @RequestParam("page_number") Integer pageNumber,
            @RequestParam("page_size") Integer pageSize
    ) {
        return ResponseEntity.ok(zalogIpotekaInterface.getAllPagination(cadastreType, state, pageNumber, pageSize));
    }

    //---------------------------zalog ipoteka pagination ---------------------------
    @PostMapping("/page/sort")
    public ResponseEntity<ResponseSortDTO> getPaginationSort(
            @RequestParam("cadastreType") Integer cadastreType,
            @RequestParam("state") Integer state,
            @RequestParam("page_number") Integer pageNumber,
            @RequestParam("page_size") Integer pageSize,
            @RequestParam("sort") String sort) {
        return ResponseEntity.ok(zalogIpotekaInterface.getAllPaginationSort(cadastreType, state, pageNumber, pageSize, sort));
    }
}