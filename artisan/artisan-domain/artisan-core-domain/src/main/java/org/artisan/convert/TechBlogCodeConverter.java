package org.artisan.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import org.artisan.core.TechBlogCode;

@Converter
public class TechBlogCodeConverter implements AttributeConverter<TechBlogCode, Integer> {
    @Override
    public Integer convertToDatabaseColumn(TechBlogCode techBlogCode) {
        return techBlogCode.getId();
    }

    @Override
    public TechBlogCode convertToEntityAttribute(Integer code) {
        return TechBlogCode.from(code);
    }
}
