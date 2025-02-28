package org.artisan.domain;

import java.time.LocalDate;
import lombok.Builder;
import org.artisan.core.TechBlogCode;

@Builder
public record ExternalBlogPost(
        String link,
        String title,
        String summary,
        String author,
        LocalDate postDate,
        String suffix,
        String thumbnailUrl,
        TechBlogCode code
) {

    public TemporalTechBlogPost toTemporal() {
        return TemporalTechBlogPost.builder()
                .title(this.title())
                .author(this.author())
                .summary(this.summary())
                .thumbnailUrl(this.thumbnailUrl())
                .url(this.link())
                .createdDate(this.postDate())
                .techBlogCode(this.code())
                .crawledDate(LocalDate.now())
                .urlSuffix(this.suffix())
                .build();
    }
}

