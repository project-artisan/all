package org.artisan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity {

    private String name;

    public Category(String name) {
        this.name = name;
    }

}
