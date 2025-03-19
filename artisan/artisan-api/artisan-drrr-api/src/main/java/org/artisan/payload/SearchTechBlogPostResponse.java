package org.artisan.payload;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.TechBlogPost;
import org.artisan.domain.file.ExternalURL;

@Builder
public record SearchTechBlogPostResponse(

        Long id,
        String title,
        ExternalURL thumbnail,
        String description,
        String link,
        TechBlogCode techBlog,
        int viewCount,

        LocalDate createdAt,

        List<SearchCategoryResponse> categories,

        boolean hasRead
) {

    public static SearchTechBlogPostResponse from(TechBlogPost post){
        return new SearchTechBlogPostResponse(
                post.getId(),
                post.getBlogMetadata().title(),
                post.getBlogMetadata().getThumbnail(),
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
