package uz.davrbank.partnervalidationservice.common.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseFilter implements Serializable {

    Integer page = 0;

    Integer size = 20;

    String search = "";

    String orderBy = "id";

    String sortOrder = "desc";

    public Integer getPage() {
        return page == null || page < 0 ? 0 : page;
    }

    public Integer getSize() {
        if (size == null || size < 1) {
            return 20;
        }
        return Math.min(size, 100);
    }

    public String getSearch() {
        return this.search = this.search != null
                ? this.search.trim().replaceAll("[!?,'{}`]", "").toLowerCase()
                : null;
    }

    public String getOrderBy() {
        return StringUtils.hasText(orderBy) ? orderBy : "id";
    }

    public String getSortOrder() {
        return StringUtils.hasText(sortOrder) ? sortOrder : "desc";
    }

    @JsonIgnore
    public boolean isSearchNotEmpty() {
        return StringUtils.hasText(getSearch());
    }

    @JsonIgnore
    public String getSearchValue() {
        return "%" + getSearch() + "%";
    }

    @JsonIgnore
    public Sort getOrderedSortBy() {
        return "asc".equalsIgnoreCase(getSortOrder())
                ? Sort.by(getOrderBy()).ascending()
                : Sort.by(getOrderBy()).descending();
    }
}
