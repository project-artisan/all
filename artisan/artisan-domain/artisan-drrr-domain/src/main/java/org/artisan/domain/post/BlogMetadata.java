package org.artisan.domain.post;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.artisan.convert.TechBlogCodeConverter;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.file.ExternalURL;


// TODO hibernate issue 올리기

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogMetadata {
    private String title;
    private String description;
    private LocalDate writtenAt;
    @Column(nullable = false)
    private String urlSuffix;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "thumbnail_id")),
            @AttributeOverride(name = "type", column = @Column(name = "thumbnail_type"))
    })
    private ExternalURL thumbnail;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "blog_id")),
            @AttributeOverride(name = "type", column = @Column(name = "blog_type"))
    })
    private ExternalURL blogLink;
    @Column(name = "tech_blog_code")
    @Convert(converter = TechBlogCodeConverter.class)
    private TechBlogCode code;

    public BlogMetadata(
            String title,
            String description,
            LocalDate writtenAt,
            String urlSuffix,
            ExternalURL thumbnail,
            ExternalURL blogLink,
            TechBlogCode code

    ) {
        this.title = title;
        this.description = description;
        this.writtenAt = writtenAt;
        this.urlSuffix = urlSuffix;
        this.thumbnail = thumbnail;
        this.blogLink = blogLink;
        this.code = code;
    }

    public String getThumbnailUrl() {
        var thumbnailUrl = thumbnail.toUrl();

        if (Strings.isBlank(thumbnailUrl)) {
            return code.getBlogLogo();
        }

        return thumbnailUrl;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public LocalDate writtenAt() {
        return writtenAt;
    }

    public String urlSuffix() {
        return urlSuffix;
    }

    public ExternalURL thumbnail() {
        return thumbnail;
    }


    public ExternalURL blogLink() {
        return blogLink;
    }

    public TechBlogCode code() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (BlogMetadata) obj;
        return Objects.equals(this.title, that.title) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.writtenAt, that.writtenAt) &&
                Objects.equals(this.urlSuffix, that.urlSuffix) &&
                Objects.equals(this.thumbnail, that.thumbnail) &&
                Objects.equals(this.blogLink, that.blogLink) &&
                Objects.equals(this.code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, writtenAt, urlSuffix, thumbnail, blogLink, code);
    }

    @Override
    public String toString() {
        return "BlogMetadata[" +
                "title=" + title + ", " +
                "description=" + description + ", " +
                "writtenAt=" + writtenAt + ", " +
                "urlSuffix=" + urlSuffix + ", " +
                "thumbnail=" + thumbnail + ", " +
                "blogLink=" + blogLink + ", " +
                "code=" + code + ']';
    }

}
