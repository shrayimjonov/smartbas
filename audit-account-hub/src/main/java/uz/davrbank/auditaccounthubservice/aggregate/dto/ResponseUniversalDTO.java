package uz.davrbank.auditaccounthubservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUniversalDTO<T> implements Serializable {
    private String status;
    private Long totalElements;
    private Long totalPages;
    private boolean last;
    private boolean first;
    private boolean empty;
    private int pageNumber;
    private int pageSize;
    private int numberOfElements;
    private List<T> data;
    private String error;
}