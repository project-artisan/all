package org.artisan.reader;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.util.fluent.cralwer.MultiPage;

@RequiredArgsConstructor
public class ParallelPageItemReader implements TechBlogReader {

    private final MultiPage<ExternalBlogPosts> pages;
    @Getter
    private final TechBlogCode techBlogCode;

    @Override
    public ExternalBlogPosts read() {
        var result = pages.execute()
                .stream()
                .flatMap(externalBlogPosts -> externalBlogPosts.posts().stream())
                .collect(collectingAndThen(toList(), ExternalBlogPosts::new));

        if (result.posts().isEmpty()) {
            return null;
        }
        return result;
    }
}
