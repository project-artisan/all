package org.artisan.reader;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.proxy.ProxyItemReader;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlerItemReaderFactory {

    private final Map<TechBlogCode, TechBlogReader> clrawerMap;

    public ProxyItemReader<ExternalBlogPosts> createItemReader(TechBlogCode code) {
        return new ProxyItemReader<>(() -> clrawerMap.get(code));
    }
}
