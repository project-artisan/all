package org.artisan.domain;

import jakarta.persistence.Entity;
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
    @JoinColumn(name = "post_id", nullable = false)
    private TechBlogPost post;


    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public TechBlogTag(TechBlogPost post, Category category) {
        this.post = post;
        this.category = category;
    }

}
