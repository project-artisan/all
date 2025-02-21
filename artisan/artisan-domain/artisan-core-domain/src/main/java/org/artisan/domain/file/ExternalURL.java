package org.artisan.domain.file;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExternalURL extends File {

    public ExternalURL(FileType type, String id) {
        super(type, id);
    }

    @Override
    public String toUrl() {
        return this.id;
    }

    public static ExternalURL from(String url){
        return new ExternalURL(FileType.URL, url);
    }
}
