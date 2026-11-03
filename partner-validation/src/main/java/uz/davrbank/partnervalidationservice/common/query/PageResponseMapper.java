package uz.davrbank.partnervalidationservice.common.query;

import org.springframework.data.domain.Page;
import uz.davrbank.partnervalidationservice.common.api.ResponsePageDTO;

public final class PageResponseMapper {

    private PageResponseMapper() {
    }

    public static <T> ResponsePageDTO<T> fromPage(Page<T> page) {
        return ResponsePageDTO.<T>builder()
                .list(page.getContent())
                .total(page.getTotalElements())
                .page(page.getNumber())
                .size(page.getSize())
                .build();
    }
}
