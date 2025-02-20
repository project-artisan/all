package org.artisan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;

@Getter
@Entity
@Table(name = "TAGS")
@NoArgsConstructor
public class TechBlogTag extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "post_id")
    private TechBlogPost post;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    public TechBlogTag(TechBlogPost post, Category category) {
        this.post = post;
        this.category = category;
    }

}
