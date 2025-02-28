package org.artisan.reader.blog;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPost;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.reader.CrawlingLocalDatePatterns;
import org.artisan.reader.ParallelPageItemReader;
import org.artisan.reader.TechBlogReader;
import org.artisan.util.fluent.cralwer.ContentsLoader;
import org.artisan.util.fluent.cralwer.ContentsReader;
import org.artisan.util.fluent.cralwer.GenericParallelPages;
import org.artisan.util.fluent.cralwer.PaginationReader;
import org.artisan.util.fluent.cralwer.PaginationReader.PaginationInformation;
import org.artisan.util.fluent.cralwer.ParallelPageInitializer;
import org.artisan.util.fluent.cralwer.ParallelPageInitializer.BasePageUrls;
import org.artisan.util.fluent.cralwer.SimpleContentsLoader;
import org.artisan.util.fluent.cralwer.WebDriverPool;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WoowahanCrawler {


    private static final String PREFIX_URL = "https://techblog.woowahan.com";

    private static final TechBlogCode CODE = TechBlogCode.WOOWAHAN;

    @Bean
    public TechBlogReader woowahanPages(WebDriverPool webDriverPool) {
        return new ParallelPageItemReader(GenericParallelPages.<ExternalBlogPosts>builder()
                .parallelCount(3)
                .webDriverPool(webDriverPool)
                .pageInitializer(pagesInitializer())
                .paginationReader(paginationReader())
                .contentsLoader(contentsLoader())
                .contentsReader(contentsReader())
                .after(data -> log.info("{}", data))
                .build(), CODE);

    }

    ParallelPageInitializer pagesInitializer() {
        return () -> new BasePageUrls(PREFIX_URL, page -> "https://techblog.woowahan.com/?paged=" + page);
    }

    ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(By.className("wp-pagenavi"));
    }

    ContentsReader<ExternalBlogPosts> contentsReader() {
        return driver -> driver.findElements(By.cssSelector("div.post-item:not(.firstpaint)"))
                .stream()
                .map(this::find)
                .collect(collectingAndThen(toList(), ExternalBlogPosts::new));
    }

    ExternalBlogPost find(WebElement article) {
        final String title = article.findElement(By.className("post-title")).getText();
        final String author = article.findElement( By.className("post-author-name")).getText();
        final String summary = article.findElement(By.className("post-excerpt")).getText();
        final String postDateText = article.findElement(By.className("post-author-date")).getText();

        // <a> 태그들을 찾아서 두 번째 링크에서 href 추출
        final var anchorElement = article.findElement(By.tagName("a"));
        final String href = anchorElement.getAttribute("href");

        // 접두어(PREFIX_URL)를 제거하여 suffix 추출
        final String suffix = extractSuffix(href);

        return ExternalBlogPost.builder()
                .link(href)
                .suffix(suffix)
                .title(title)
                .author(author)
                .summary(summary)
                .postDate(CrawlingLocalDatePatterns.PATTERN3.parse(postDateText))
                .code(CODE)
                .build();
    }
    private String extractSuffix(String href) {
        return href.substring(WoowahanCrawler.PREFIX_URL.length()).replace("/", "");
    }
    PaginationReader paginationReader() {
        return driver -> {
            var lastElement = driver.findElement(By.className("last"));
            var href = lastElement.getAttribute("href");

            // 정규표현식을 사용하여 paged 값 추출
            var pattern = Pattern.compile("paged=(\\d+)");
            var matcher = pattern.matcher(href);
            int lastPage;
            if (matcher.find()) {
                lastPage = Integer.parseInt(matcher.group(1));
            } else {
                throw new IllegalArgumentException("paged 값을 찾지 못했습니다: " + href);
            }

            return PaginationInformation.lastPage(lastPage);
        };
    }

}
