package uz.davrbank.zalogautohubservice.endpoint;

import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.zalogautohubservice.service.cases.ZalogAutoInterface;

@RestController
@RequestMapping("/api/v1")
public class ZalogAutoEndpoint {

    private final ZalogAutoInterface zalogAutoInterface;

    public ZalogAutoEndpoint(ZalogAutoInterface zalogAutoInterface) {
        this.zalogAutoInterface = zalogAutoInterface;
    }

    @PostMapping("/report/zalog/auto/get/all")
    public ResponseEntity<?> getAllNoPagination(@RequestParam("start_date") String startDate,
                                                @RequestParam("end_date") String endDate,
                                                @RequestParam("state") Integer State) {
       return ResponseEntity.ok(zalogAutoInterface.getAllNoPagination(startDate, endDate, State));
    }

    @PostMapping("/report/zalog/auto/get/all/page")
    public ResponseEntity<?> getAllPagination(@RequestParam("start_date") String startDate,
                                                @RequestParam("end_date") String endDate,
                                                @RequestParam("state") Integer State,
                                                @RequestParam("page_number") Integer PageNumber,
                                                @RequestParam("page_size") Integer PageSize) {
        return ResponseEntity.ok(zalogAutoInterface.getAllPagination(startDate, endDate, State, PageNumber, PageSize));
    }
}