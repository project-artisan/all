package org.artisan.domain;

import java.util.List;

public record ExternalBlogPosts(
        List<ExternalBlogPost> posts
) {
}
