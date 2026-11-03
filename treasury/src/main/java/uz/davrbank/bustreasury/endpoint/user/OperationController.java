package uz.davrbank.bustreasury.endpoint.user;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.bustreasury.aggregate.dto.request.user.OperationUserRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.user.OperationUserResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.user.OperationWithWiringDTO;
import uz.davrbank.bustreasury.service.cases.user.TemplateResolverService;
import uz.davrbank.bustreasury.service.interfaces.user.WiringInterfaceUSER;
import uz.davrbank.bustreasury.service.interfaces.user.WiringOperations;
import uz.davrbank.bustreasury.util.CurrencyType;
import uz.davrbank.bustreasury.util.OperationType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(
        name = "Операции по проводкам", description = "Операции с проводками по валютам"
)
public class OperationController {

    private final WiringInterfaceUSER buyService;
    private final WiringInterfaceUSER sellService;
    private final WiringInterfaceUSER wiringServiceUSERBuyRUB;
    private final WiringInterfaceUSER wiringServiceUSERSellRUB;
    private final WiringInterfaceUSER wiringInterfaceUSERBuyEURO;
    private final WiringInterfaceUSER wiringInterfaceUSERSellEUR;
    private final TemplateResolverService templateResolverService;
    private final WiringOperations wiringOperations;

    public OperationController(
            @Qualifier("wiringServiceUSERBuy") WiringInterfaceUSER buyService,
            @Qualifier("wiringServiceUSERSell") WiringInterfaceUSER sellService,
            @Qualifier("wiringServiceUSERBuyRUB") WiringInterfaceUSER wiringServiceUSERBuyRUB1,
            @Qualifier("wiringServiceUSERSellRUB") WiringInterfaceUSER wiringServiceUSERSellRUB,
            @Qualifier("wiringServiceUSERBuyEUR") WiringInterfaceUSER wiringInterfaceUSERBuyEURO,
            @Qualifier("wiringServiceUSERSellEUR") WiringInterfaceUSER wiringInterfaceUSERSellEUR,
            TemplateResolverService templateResolverService,
            WiringOperations wiringOperations
    ) {
        this.buyService = buyService;
        this.sellService = sellService;
        this.wiringServiceUSERBuyRUB = wiringServiceUSERBuyRUB1;
        this.wiringServiceUSERSellRUB = wiringServiceUSERSellRUB;
        this.wiringInterfaceUSERBuyEURO = wiringInterfaceUSERBuyEURO;
        this.wiringInterfaceUSERSellEUR = wiringInterfaceUSERSellEUR;
        this.templateResolverService = templateResolverService;
        this.wiringOperations = wiringOperations;
    }

    @PermitAll
    @PostMapping("/usd/buy")
    @Operation(
            summary = "Покупка USD",
            description = "Запускает процесс проводок для покупки USD, шаблон определяется автоматически по USD/BUY."
    )
    public ResponseEntity<OperationUserResponseDTO> buy(@RequestBody OperationUserRequestDTO requestDTO) {
        Long templateId = templateResolverService.resolve(CurrencyType.USD, OperationType.BUY);
        requestDTO.setTemplateId(String.valueOf(templateId));
        return ResponseEntity.ok(buyService.createWiringOperations(requestDTO));
    }

    @PermitAll
    @PostMapping("/usd/sell")
    @Operation(
            summary = "Продажа USD",
            description = "Отправляет проводки для продажи USD, шаблон автоматически определяется по USD/SELL."
    )
    public ResponseEntity<OperationUserResponseDTO> sell(@RequestBody OperationUserRequestDTO requestDTO) {
        Long tempId = templateResolverService.resolve(CurrencyType.USD, OperationType.SELL);
        requestDTO.setTemplateId(String.valueOf(tempId));
        return ResponseEntity.ok(sellService.createWiringOperations(requestDTO));
    }

    @PermitAll
    @PostMapping("/ruble/buy")
    @Operation(
            summary = "Покупка RUB",
            description = "Проводит покупку RUB и отправляет проводки последовательно."
    )
    public ResponseEntity<OperationUserResponseDTO> buyRub(@RequestBody OperationUserRequestDTO requestDTO) {
        Long templateId = templateResolverService.resolve(CurrencyType.RUB, OperationType.BUY);
        requestDTO.setTemplateId(String.valueOf(templateId));
        return ResponseEntity.ok(wiringServiceUSERBuyRUB.createWiringOperations(requestDTO));
    }

    @PermitAll
    @PostMapping("/ruble/sell")
    @Operation(
            summary = "Продажа RUB",
            description = "Выполняет продажу RUB и отправляет проводки в систему."
    )
    public ResponseEntity<OperationUserResponseDTO> sellRub(@RequestBody OperationUserRequestDTO requestDTO) {
        Long templateId = templateResolverService.resolve(CurrencyType.RUB, OperationType.SELL);
        requestDTO.setTemplateId(String.valueOf(templateId));
        return ResponseEntity.ok(wiringServiceUSERSellRUB.createWiringOperations(requestDTO));
    }

    @PermitAll
    @PostMapping("/euro/buy")
    @Operation(
            summary = "Покупка EUR",
            description = "Выполняет покупку EUR и отправляет связанные проводки последовательно."
    )
    public ResponseEntity<OperationUserResponseDTO> buyEuro(@RequestBody OperationUserRequestDTO requestDTO) {
        Long templateId = templateResolverService.resolve(CurrencyType.EUR, OperationType.BUY);
        requestDTO.setTemplateId(String.valueOf(templateId));
        return ResponseEntity.ok(wiringInterfaceUSERBuyEURO.createWiringOperations(requestDTO));
    }

    @PermitAll
    @PostMapping("/euro/sell")
    @Operation(
            summary = "Продажа EUR",
            description = "Отправляет проводки для операции продажи EUR."
    )
    public ResponseEntity<OperationUserResponseDTO> sellEuro(@RequestBody OperationUserRequestDTO requestDTO) {
        Long templateId = templateResolverService.resolve(CurrencyType.EUR, OperationType.SELL);
        requestDTO.setTemplateId(String.valueOf(templateId));
        return ResponseEntity.ok(wiringInterfaceUSERSellEUR.createWiringOperations(requestDTO));
    }

    @PermitAll
    @GetMapping("/all/wiring")
    @Operation(
            summary = "Все отправленные проводки",
            description = "Возвращает все записи из таблицы SentWiring (последние в начале списка)."
    )
    public ResponseEntity<Page<OperationWithWiringDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(wiringOperations.getAllWithWires(pageable));
    }

    @PermitAll
    @GetMapping("/operation/{operationId}")
    @Operation(summary = "Операция по ID", description = "Возвращает одну операцию по её ID, включая все проводки внутри.")
    public ResponseEntity<OperationWithWiringDTO> getOne(@PathVariable Long operationId) {
        return ResponseEntity.ok(wiringOperations.getOneWithWiresByOperationId(operationId));
    }

    @PermitAll
    @GetMapping("/byTemplate/{templateId}")
    @Operation(summary = "Операции по шаблону (paged)", description = "Возвращает все операции для указанного templateId, с проводками внутри.")
    public ResponseEntity<Page<OperationWithWiringDTO>> getAllByTemplate(@PathVariable String templateId, Pageable pageable
    ) {
        return ResponseEntity.ok(wiringOperations.getAllByTemplate(templateId, pageable));
    }
}
