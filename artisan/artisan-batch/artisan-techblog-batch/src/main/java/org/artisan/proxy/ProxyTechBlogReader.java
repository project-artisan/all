package org.artisan.proxy;

import java.util.Objects;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.reader.TechBlogReader;

@RequiredArgsConstructor
public class ProxyTechBlogReader implements TechBlogReader {
    private final Supplier<TechBlogReader> techBlogReaderSupplier;
    private final TechBlogCode code;
    private TechBlogReader techBlogReader;

    @Override
    public TechBlogCode getTechBlogCode() {
        return this.code;
    }

    @Override
    public ExternalBlogPosts read() throws Exception {
        if (Objects.isNull(techBlogReader)) {
            this.techBlogReader = techBlogReaderSupplier.get();
        }
        return this.techBlogReader.read();
    }
}

