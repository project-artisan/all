package org.artisan.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.TechBlogCode;
import org.artisan.core.domain.BaseEntity;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tehc_blog_post")
public class TechBlogPost extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(length = 255)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_blog_code")
    private TechBlogCode code;

    @Column(nullable = false)
    private String urlSuffix;

    @Column(nullable = false, columnDefinition = "TEXT", length = 700)
    private String url;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int postLike = 0;

    private LocalDate writtenAt;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private final List<TechBlogTag> tags = new ArrayList<>();

    @Builder
    public TechBlogPost(
            String title,
            TechBlogCode code,
            String urlSuffix,
            String url,
            LocalDate writtenAt
    ) {
        this.title = title;
        this.code = code;
        this.url = url;
        this.urlSuffix = urlSuffix;
        this.writtenAt = writtenAt;
    }

    public void increaseViewCount() {
        viewCount += 1;
    }

    public void increasePostLike() {
        postLike += 1;
    }

    public void decreasePostLike() {
        postLike -= 1;
    }

    public String getThumbnailUrl() {
        if (thumbnailUrl == null) {
            return code.getBlogLogo();
        }
        return thumbnailUrl;
    }
}
