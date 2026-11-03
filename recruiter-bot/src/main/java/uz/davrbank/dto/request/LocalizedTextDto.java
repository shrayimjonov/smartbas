package uz.davrbank.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.context.i18n.LocaleContextHolder;
import uz.davrbank.config.annotations.AtLeastOneNotNull;

import java.util.Locale;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AtLeastOneNotNull(fields = {"uz", "ru", "en"}, message = "At least one of 'uz' or 'ru' must be provided and not null")
public class LocalizedTextDto {
    private String uz;
    private String ru;
    private String en;

    public String getText(String lang) {
        if (lang == null || lang.isEmpty()) {
            Locale locale = LocaleContextHolder.getLocale();
            lang = locale.getLanguage();
        }
        return switch (lang) {
            case "en" -> this.en;
            case "ru" -> this.ru;
            default -> this.uz; // Default to Uzbek
        };
    }


}
