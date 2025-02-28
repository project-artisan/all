package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.artisan.convert.TechBlogCodeConverter;
import org.artisan.core.TechBlogCode;
import org.artisan.core.domain.BaseEntity;
import org.artisan.domain.file.ExternalURL;
import org.artisan.domain.post.BlogMetadata;

@ToString
@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporalTechBlogPost extends BaseEntity {

    @Column(nullable = false)
    private LocalDate writtenAt;

    @Column
    private String author;
    @Column(length = 500)
    private String thumbnailUrl;

    @Column(nullable = false)
    private String title;

    // 설명이 없는 기술블로그가 있음
    @Column(length = 1000)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String aiSummary;

    @Column(nullable = false)
    private String urlSuffix;

    @Column(nullable = false)
    private String url;

    @Convert(converter = TechBlogCodeConverter.class)
    private TechBlogCode techBlogCode;

    @Column(nullable = false)
    private LocalDate crawledDate;

    @Column(nullable = false)
    private boolean registrationCompleted;

    @Builder
    public TemporalTechBlogPost(
            LocalDate createdDate,
            String author,
            String thumbnailUrl,
            String title,
            String summary,
            String aiSummary,
            String urlSuffix,
            String url,
            TechBlogCode techBlogCode,
            LocalDate crawledDate

    ) {
        this.writtenAt = createdDate;
        this.author = author;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.summary = summary;
        this.aiSummary = aiSummary;
        this.urlSuffix = urlSuffix;
        this.url = url;
        this.techBlogCode = techBlogCode;
        this.crawledDate = crawledDate;
        this.registrationCompleted = true;
    }

    public TechBlogPost toTechBlogPost() {
        log.info("{}", this);
        var metadata = new BlogMetadata(
                title,
                this.summary,
                this.writtenAt,
                this.urlSuffix,
                ExternalURL.from(this.thumbnailUrl),
                ExternalURL.from(this.url),
                this.techBlogCode
        );
        log.info("{}", metadata);
        return TechBlogPost.from(metadata);
    }
}
