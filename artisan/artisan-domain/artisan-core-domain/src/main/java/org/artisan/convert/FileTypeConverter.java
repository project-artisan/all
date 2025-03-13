package org.artisan.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.artisan.domain.file.FileType;

@Converter
public class FileTypeConverter implements AttributeConverter<FileType, String> {

    @Override
    public String convertToDatabaseColumn(FileType fileType) {
        if(fileType == null){
            return "";
        }
        return fileType.getValue();
    }

    @Override
    public FileType convertToEntityAttribute(String s) {
        if(s == null){
            return null;
        }
        return FileType.from(s);
    }
}
