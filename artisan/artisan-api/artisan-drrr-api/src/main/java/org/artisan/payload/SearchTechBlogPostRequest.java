package org.artisan.payload;

import java.util.List;
import org.artisan.core.TechBlogCode;

public record SearchTechBlogPostRequest(
        String title,
        List<TechBlogCode> select
){
}
