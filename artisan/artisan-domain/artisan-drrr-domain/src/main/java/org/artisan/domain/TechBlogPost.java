package org.artisan.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import org.artisan.domain.post.BlogMetadata;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tech_blog_posts")
public class TechBlogPost extends BaseEntity {

    @Embedded
    private BlogMetadata blogMetadata;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int postLike = 0;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private final List<TechBlogTag> tags = new ArrayList<>();


    protected TechBlogPost(BlogMetadata blogMetadata) {
        this.blogMetadata = blogMetadata;
        this.viewCount = 0;
        this.postLike = 0;
    }

    public static TechBlogPost from(BlogMetadata metadata) {
        return new TechBlogPost(metadata);
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
}
