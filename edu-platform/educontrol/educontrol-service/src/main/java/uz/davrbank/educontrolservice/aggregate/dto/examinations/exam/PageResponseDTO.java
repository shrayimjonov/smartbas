package uz.davrbank.educontrolservice.aggregate.dto.examinations.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDTO<T> {
    private List<T> content;
    private long totalElements;
    private long totalPages;
    private int number; // Current page number
    private int size;   // Page size
}