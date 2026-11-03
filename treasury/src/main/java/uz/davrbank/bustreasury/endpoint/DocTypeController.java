package uz.davrbank.bustreasury.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.bustreasury.aggregate.dto.request.DocTypeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.DocTypeResponseDTO;
import uz.davrbank.bustreasury.service.interfaces.DocTypeInterface;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/doctype")
@Tag(
        name = "Типы документов",
        description = "Контроллер для управления типами документов — создание, получение, обновление и удаление."
)
public class DocTypeController {

    private final DocTypeInterface docTypeInterface;

    public DocTypeController(DocTypeInterface docTypeInterface) {
        this.docTypeInterface = docTypeInterface;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание типа документа",
            description = "Добавляет новый тип документа в систему."
    )
    public ResponseEntity<?> createDocType(@RequestBody DocTypeRequestDTO requestDTO) {
        return ResponseEntity.ok(docTypeInterface.createDocType(requestDTO));
    }

    @GetMapping("/get/{id}")
    @Operation(
            summary = "Получение типа документа по ID",
            description = "Возвращает информацию о типе документа по его уникальному идентификатору."
    )
    public ResponseEntity<DocTypeResponseDTO> getDocTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(docTypeInterface.getDocTypeById(id));
    }

    @GetMapping("/get/all")
    @Operation(
            summary = "Получение всех типов документов",
            description = "Возвращает список всех зарегистрированных типов документов."
    )
    public ResponseEntity<List<DocTypeResponseDTO>> getAllDocTypes() {
        return ResponseEntity.ok(docTypeInterface.getAllDocTypes());
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Обновление типа документа",
            description = "Изменяет данные существующего типа документа по указанному идентификатору."
    )
    public ResponseEntity<DocTypeResponseDTO> updateDocType(
            @PathVariable Long id,
            @RequestBody DocTypeRequestDTO requestDTO) {
        return ResponseEntity.ok(docTypeInterface.updateDocType(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление типа документа",
            description = "Удаляет тип документа из системы по указанному идентификатору."
    )
    public ResponseEntity<Void> deleteDocType(@PathVariable Long id) {
        docTypeInterface.deleteDocType(id);
        return ResponseEntity.ok().build();
    }
}
