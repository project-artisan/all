package org.artisan.payload;

import org.artisan.core.TechBlogCode;

public record TechBlogCodeResponse(
        int id,
        String name,
        String title,
        String logo,
        String url
) {
    public static TechBlogCodeResponse from(TechBlogCode code) {
        return new TechBlogCodeResponse(
                code.getId(),
                code.name(),
                code.getTitle(),
                code.getMetadata().getLogo(),
                code.getMetadata().getUrl()
        );
    }
}
