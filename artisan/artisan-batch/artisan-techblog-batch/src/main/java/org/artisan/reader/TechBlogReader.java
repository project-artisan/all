package org.artisan.reader;


import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPosts;
import org.springframework.batch.item.ItemReader;

public interface TechBlogReader extends ItemReader<ExternalBlogPosts> {

    TechBlogCode getTechBlogCode();
}
