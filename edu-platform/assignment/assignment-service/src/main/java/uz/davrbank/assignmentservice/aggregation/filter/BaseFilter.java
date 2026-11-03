package uz.davrbank.assignmentservice.aggregation.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseFilter {

    Integer page = 0;

    Integer size = 20;

    String search = "";

    String orderBy = "id";

    String sortOrder = "desc";

    public String getSearch() {
        return this.search = this.search != null ? this.search.trim().replaceAll("[!?,'{}`]", "").toLowerCase() : null;
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
    public String getLikeSearchKey() {
        return " like (:searchKey)";
    }

    @JsonIgnore
    public Sort getOrderedSortBy() {
        return sortOrder.equals("asc") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
    }

    @JsonIgnore
    public int getStart() {
        return this.getPage() * this.getSize();
    }

    public String getOrderBy() {
        return StringUtils.hasText(orderBy) ? orderBy : "id";
    }
}
