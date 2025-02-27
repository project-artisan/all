package org.artisan.domain;


import java.util.List;
import org.artisan.core.TechBlogCode;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.HashMap;


@Component
public class CrawledTechBlogPostRepository {

    private final HashMap<Key, ExternalBlogPost> postHashMap = new HashMap<>();

    public void insert(ExternalBlogPost externalBlogPost) {
        this.postHashMap.put(Key.from(externalBlogPost), externalBlogPost);
    }

    public boolean containsKey(String suffix, TechBlogCode techBlogCode) {
        return this.postHashMap.containsKey(new Key(suffix, techBlogCode));
    }

    public boolean remove(String suffix, TechBlogCode techBlogCode) {
        return this.postHashMap.containsKey(new Key(suffix, techBlogCode));
    }

    public void remove(Key key) {
        this.postHashMap.remove(key);
    }

    public void clear() {
        this.postHashMap.clear();
    }

    public void insertAll(ExternalBlogPosts externalBlogPosts) {
        externalBlogPosts.posts().forEach(this::insert);
    }

    public int count() {
        return postHashMap.size();
    }

    public void ifPresentOrElse(Key key, Runnable presentRunnable, Runnable orElseRunner) {
        if (postHashMap.containsKey(key)) {
            presentRunnable.run();
            return;
        }
        orElseRunner.run();
    }

    public List<TemporalTechBlogPost> toTemporalTechBlogPosts() {
        return postHashMap.values()
                .stream()
                .map(ExternalBlogPost::toTemporal)
                .toList();
    }

    public record Key(String suffix, TechBlogCode techBlogCode) {
        public static Key from(ExternalBlogPost externalBlogPost) {
            return new Key(externalBlogPost.suffix(), externalBlogPost.code());
        }
    }
}

