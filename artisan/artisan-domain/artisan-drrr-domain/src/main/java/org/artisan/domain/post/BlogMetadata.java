package org.artisan.domain.post;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.time.LocalDateTime;
import org.apache.logging.log4j.util.Strings;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.file.ExternalURL;

@Embeddable
public record BlogMetadata(
        String title,

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "id", column = @Column(name = "thumbnail_id")),
                @AttributeOverride(name = "type", column = @Column(name ="thumbnail_type"))
        })
        ExternalURL thumbnail,
        String description,
        @Column(name = "tech_blog_code")
        TechBlogCode code,

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "id", column = @Column(name = "blog_id")),
                @AttributeOverride(name = "type", column = @Column(name ="blog_type"))
        })
        ExternalURL blogLink,
        LocalDateTime writtenAt,
        @Column(nullable = false) String urlSuffix
){
    public String getThumbnailUrl() {
        var thumbnailUrl = thumbnail.toUrl();

        if (Strings.isBlank(thumbnailUrl)) {
            return code.getBlogLogo();
        }

        return thumbnailUrl;
    }
}
