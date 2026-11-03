package uz.davrbank.esudservice.utils.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static uz.davrbank.esudservice.utils.DateUtil.formatDate;


public class CustomDateSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate date,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializers) throws IOException {
        if (Objects.nonNull(date)) {
            jsonGenerator.writeString(formatDate(date));
        }
    }
}
