package org.artisan.reader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.util.fluent.cralwer.Page;
import org.springframework.batch.item.ItemReader;

@RequiredArgsConstructor
public class PageItemReader implements ItemReader<ExternalBlogPosts>, TechBlogReader {
    private final Page<ExternalBlogPosts> pages;

    @Getter
    private final TechBlogCode techBlogCode;


    @Override
    public ExternalBlogPosts read() {
        return pages.execute();
    }


}
