package org.artisan.domain.file;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.artisan.convert.FileTypeConverter;

@Getter
@Embeddable
@NoArgsConstructor
public class ExternalURL {

    @Convert(converter = FileTypeConverter.class)
    @Column(nullable = false, name = "file_type")
    private FileType type;

    @Column(nullable = false, name = "file_id", length = 700)
    private String id;

    public ExternalURL(FileType type, String id) {
        this.type = type;
        this.id = id;
    }

    public String toUrl() {
        return this.id;
    }

    public static ExternalURL from(String url){
        return new ExternalURL(FileType.URL, url);
    }

    @Override
    public String toString() {
        return "ExternalURL{" +
                "type=" + type +
                ", id='" + id + '\'' +
                '}';
    }
}
