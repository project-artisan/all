package org.artisan.payload;

import org.artisan.core.TechBlogCode;

public record TechBlogCodeResponse(
        Integer id,
        String name
) {
    public static TechBlogCodeResponse from(TechBlogCode code) {
        return new TechBlogCodeResponse(code.getId(), code.getName());
    }
}
