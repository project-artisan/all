package org.artisan.payload;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.TechBlogPost;
import org.artisan.domain.post.BlogMetadata;

@Builder
public record SearchTechBlogPostResponse(

        Long id,
        String title,
        String thumbnail,
        String description,
        String link,
        TechBlogCode techBlog,
        int viewCount,

        LocalDateTime createdAt,

        List<SearchCategoryResponse> categories,

        boolean hasRead
) {

    public static SearchTechBlogPostResponse from(TechBlogPost post){
        return new SearchTechBlogPostResponse(
                post.getId(),
                post.getBlogMetadata().title(),
                post.getBlogMetadata().getThumbnailUrl(),
                post.getBlogMetadata().description(),
                post.getBlogMetadata().blogLink().toUrl(),
                post.getBlogMetadata().code(),
                post.getViewCount(),
                post.getBlogMetadata().writtenAt(),
                List.of(),
                false
        );
    }
}
