package RBPO.proj.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/** Принимает yyyy-MM-dd или ISO date-time (берётся календарная дата). */
public class VisitDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String s = p.getValueAsString();
        if (s == null || s.isBlank()) {
            return null;
        }
        s = s.trim();
        try {
            if (s.length() <= 10) {
                return LocalDate.parse(s);
            }
            return LocalDateTime.parse(s).toLocalDate();
        } catch (DateTimeParseException e) {
            throw ctxt.weirdStringException(s, LocalDate.class, e.getMessage());
        }
    }
}
