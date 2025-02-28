package org.artisan.reader.blog;

import static java.util.stream.Collectors.toList;

import java.util.stream.Collectors;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPost;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.proxy.ProxyTechBlogReader;
import org.artisan.reader.CrawlingLocalDatePatterns;
import org.artisan.reader.EmptyFinder;
import org.artisan.reader.PageItemReader;
import org.artisan.reader.TechBlogReader;
import org.artisan.util.fluent.cralwer.ContentsLoader;
import org.artisan.util.fluent.cralwer.ContentsReader;
import org.artisan.util.fluent.cralwer.SimpleContentsLoader;
import org.artisan.util.fluent.cralwer.SinglePage;
import org.artisan.util.fluent.cralwer.WebDriverPool;
import org.openqa.selenium.By;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class MargetKurlyCrawler {

    private static final String BASE_URL = "https://helloworld.kurly.com";
    private static final String BLOG_PREFIX = "https://helloworld.kurly.com/blog";

    private static final TechBlogCode CODE = TechBlogCode.MARKET_KURLY;

    @Bean
    public TechBlogReader marketKurly(WebDriverPool webDriverPool) {
        return new ProxyTechBlogReader(() -> {
            var page = SinglePage.<ExternalBlogPosts>builder()
                    .singlePageInitializer(() -> BASE_URL)
                    .contentsLoader(contentsLoader())
                    .contentsReader(contentsReader())
                    .after(null)
                    .webDriver(webDriverPool.borrow())
                    .webDriverCleaner(webDriverPool::returnObject)
                    .build();
            return new PageItemReader(page, CODE);
        }, CODE);
    }

    ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(By.className("post-list"));
    }

    ContentsReader<ExternalBlogPosts> contentsReader(){
        return webDriver -> webDriver.findElement(By.className("post-list"))
                .findElements(By.className("post-card"))
                .stream()
                .map(webElement -> {
                    var postLink = EmptyFinder.get(() -> webElement.findElement(By.tagName("a")))
                            .orElseThrow(IllegalArgumentException::new);
                    var postTitle = EmptyFinder.get(() -> postLink.findElement(By.className("post-title")).getText()).get();
                    var postSummary = EmptyFinder.get(() -> postLink.findElement(By.className("title-summary")).getText()).orElse("");
                    var postMeta = EmptyFinder.get(() -> webElement.findElement(By.className("post-meta"))).get();
                    var postAuthor = EmptyFinder.get(() -> postMeta.findElement(By.className("post-autor")).getText())
                            .orElse("");
                    var postDate = EmptyFinder.get(() -> postMeta.findElement(By.className("post-date")).getText())
                            .orElse("");

                    return ExternalBlogPost.builder()
                            .title(postTitle)
                            .summary(postSummary)
                            .author(postAuthor)
                            .postDate(CrawlingLocalDatePatterns.PATTERN2.parse(postDate))
                            .link(postLink.getAttribute("href"))
                            .suffix(postLink.getAttribute("href").substring(BLOG_PREFIX.length() + 1))
                            .code(CODE)
                            .build();
                }).collect(Collectors.collectingAndThen(toList(), ExternalBlogPosts::new));
    }
}
