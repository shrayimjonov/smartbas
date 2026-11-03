package uz.davrbank.esudservice.utils.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static uz.davrbank.esudservice.utils.DateUtil.formatDateTime;


public class CustomDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime dateTime,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (Objects.nonNull(dateTime)) {
            jsonGenerator.writeString(formatDateTime(dateTime));
        }
    }
}
