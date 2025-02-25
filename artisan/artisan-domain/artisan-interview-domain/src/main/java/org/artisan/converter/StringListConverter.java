package org.artisan.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final char DELIMITER = ',';

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return Strings.join(strings, DELIMITER);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        if (Objects.isNull(s)) {
            return Collections.emptyList();
        }
        return Arrays.stream(s.split(","))
                .toList();
    }
}

