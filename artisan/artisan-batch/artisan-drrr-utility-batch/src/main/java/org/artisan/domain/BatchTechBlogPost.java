package org.artisan.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import org.artisan.core.domain.BaseEntity;
import org.artisan.domain.file.ExternalURL;

@Getter
@Entity
@Table(name = "tech_blog_posts")
public class BatchTechBlogPost extends BaseEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "thumbnail_id")),
            @AttributeOverride(name = "type", column = @Column(name = "thumbnail_type"))
    })
    private ExternalURL thumbnail;

    public void changeThumbnail(ExternalURL thumbnail) {
        if(Objects.isNull(thumbnail)) {
            throw new IllegalArgumentException("존재하지 않는 썸네일입니다.");
        }

        this.thumbnail = thumbnail;
    }

}
