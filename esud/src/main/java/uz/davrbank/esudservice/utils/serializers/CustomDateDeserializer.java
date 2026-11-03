package uz.davrbank.esudservice.utils.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomDateDeserializer extends JsonDeserializer<LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public LocalDate deserialize(JsonParser jsonParser,
                                 DeserializationContext context) throws IOException {
        var dateStr = jsonParser.getValueAsString();
        if (StringUtils.hasText(dateStr)) {
            return LocalDate.parse(dateStr, formatter);
        }
        return null;
    }
}
