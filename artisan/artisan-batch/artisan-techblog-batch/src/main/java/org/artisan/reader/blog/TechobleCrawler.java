package org.artisan.reader.blog;

import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPost;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.proxy.ProxyTechBlogReader;
import org.artisan.reader.CrawlingUtils;
import org.artisan.reader.EmptyFinder;
import org.artisan.reader.PageItemReader;
import org.artisan.reader.TechBlogReader;
import org.artisan.util.fluent.cralwer.ContentsLoader;
import org.artisan.util.fluent.cralwer.ContentsReader;
import org.artisan.util.fluent.cralwer.SimpleContentsLoader;
import org.artisan.util.fluent.cralwer.SinglePage;
import org.artisan.util.fluent.cralwer.WebDriverPool;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TechobleCrawler {

    private static final String TARGET_URL = "https://tecoble.techcourse.co.kr/";
    private static final TechBlogCode CODE = TechBlogCode.TECHOBLE;

    @Bean
    public TechBlogReader techoblePage(WebDriverPool webDriverPool) {
        return new ProxyTechBlogReader(() -> {
            var page = SinglePage.<ExternalBlogPosts>builder()
                    .singlePageInitializer(() -> TARGET_URL)
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
        return new SimpleContentsLoader(By.tagName("body"));
    }

    ContentsReader<ExternalBlogPosts> contentsReader(){
        return webDriver -> {
            // TODO 스크롤 all 속성 추가할 것
            CrawlingUtils.renderAll(webDriver);

            List<WebElement> cards = webDriver.findElements(By.tagName("article"));

            // virtual thread 기반 ExecutorService (Java 19 이상)
            try (ExecutorService executor = newVirtualThreadPerTaskExecutor()) {

                // 각 card를 virtual thread에서 처리하도록 CompletableFuture 생성
                List<CompletableFuture<ExternalBlogPost>> futures = cards.stream()
                        .map(card -> CompletableFuture.supplyAsync(() -> {
                            var imageSrc = EmptyFinder.get(() -> card.findElement(By.className("post-card-image"))
                                    .findElement(By.tagName("picture"))
                                    .findElement(By.tagName("img"))
                                    .getAttribute("src"));
                            var content = card.findElement(By.className("post-card-content"));
                            var footer = content.findElement(By.className("post-card-meta"));

                            var author = content.findElement(By.className("post-card-byline-content"))
                                    .findElement(By.tagName("a"));
                            var link = content.findElement(By.className("post-card-content-link")).getAttribute("href");
                            var title = content.findElement(By.className("post-card-title"));
                            var summary = content.findElement(By.className("post-card-excerpt"));
                            var time = footer.findElement(By.tagName("time")).getAttribute("datetime");

                            ExternalBlogPost post = ExternalBlogPost.builder()
                                    .code(CODE)
                                    .title(title.getText())
                                    .suffix(link.substring(TARGET_URL.length()))
                                    .link(link)
                                    .author(author.getText())
                                    .summary(summary.getText())
                                    .thumbnailUrl(imageSrc.orElse(""))
                                    .postDate(LocalDate.parse(time))
                                    .build();

                            // 로깅 (필요에 따라 로깅 프레임워크 사용)
                            return post;
                        }, executor))
                        .toList();

                // 모든 작업의 결과를 수집
                List<ExternalBlogPost> posts = futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());

                return new ExternalBlogPosts(posts);
            }

            /*return webDriver.findElements(By.tagName("article"))
                    .parallelStream()
                    .map(card -> {
                        var imageSrc = EmptyFinder.get(() -> card.findElement(By.className("post-card-image"))
                                .findElement(By.tagName("picture"))
                                .findElement(By.tagName("img"))
                                .getAttribute("src"));
                        var content = card.findElement(By.className("post-card-content"));
                        var footer = content.findElement(By.className("post-card-meta"));

                        var author = content.findElement(By.className("post-card-byline-content"))
                                .findElement(By.tagName("a"));
                        var link = content.findElement(By.className("post-card-content-link")).getAttribute("href");
                        var title = content.findElement(By.className("post-card-title"));
                        var summary = content.findElement(By.className("post-card-excerpt"));
                        var time = footer.findElement(By.tagName("time")).getAttribute("datetime");

                        return ExternalBlogPost.builder()
                                .code(CODE)
                                .title(title.getText())
                                .suffix(link.substring(TARGET_URL.length()))
                                .link(link)
                                .author(author.getText())
                                .summary(summary.getText())
                                .thumbnailUrl(imageSrc.orElse(""))
                                .postDate(LocalDate.parse(time))
                                .build();
                    })
                    .peek(data -> log.info("{}", data))
                    .collect(Collectors.collectingAndThen(toList(), ExternalBlogPosts::new));*/
        };
    }
}
