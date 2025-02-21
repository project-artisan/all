package org.artisan.domain.file;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public abstract class File {

    @Column(nullable = false, name = "file_type")
    protected FileType type;

    @Column(nullable = false, name = "file_id", length = 700)
    protected String id;

    protected File(FileType type, String id) {
        this.type = type;
        this.id = id;
    }

    abstract public String toUrl();
}
