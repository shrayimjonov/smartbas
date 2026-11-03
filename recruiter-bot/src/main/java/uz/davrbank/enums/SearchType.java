package uz.davrbank.enums;

import lombok.Getter;

@Getter
public enum SearchType {
    title("title"),
    description("description");

    private final String type;

    SearchType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
