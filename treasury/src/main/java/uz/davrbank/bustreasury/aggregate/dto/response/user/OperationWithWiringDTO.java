package uz.davrbank.bustreasury.aggregate.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.bustreasury.aggregate.model.user.Operations;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationWithWiringDTO {

    private Long operationId;
    private String templateId;
    private LocalDateTime date;
    private String course;
    private String summa;
    private Integer totalWires;
    private String statusSummary;

    private List<SentWiringDTO> wires;

    public static OperationWithWiringDTO of(Operations op, List<SentWiringDTO> wires) {
        var dto = new OperationWithWiringDTO();
        dto.setOperationId(op.getId());
        dto.setTemplateId(op.getTemplateId());
        dto.setDate(op.getDate());
        dto.setCourse(op.getCourse());
        dto.setSumma(op.getSumma());
        dto.setWires(wires);
        dto.setTotalWires(wires != null ? wires.size() : 0);
        if (wires != null) {
            long ok = wires.stream().filter(w -> "APPROVED".equalsIgnoreCase(w.getFinaleState())).count();
            long del = wires.stream().filter(w -> "DELETED".equalsIgnoreCase(w.getFinaleState())).count();
            long to = wires.stream().filter(w -> "TIMEOUT".equalsIgnoreCase(w.getFinaleState())).count();
            dto.setStatusSummary("APPROVED=" + ok + ", DELETED=" + del + ", TIMEOUT=" + to);
        }
        return dto;
    }
}
