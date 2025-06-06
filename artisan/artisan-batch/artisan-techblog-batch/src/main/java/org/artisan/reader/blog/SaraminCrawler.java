package org.artisan.reader.blog;


import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import lombok.extern.slf4j.Slf4j;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPost;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.proxy.ProxyTechBlogReader;
import org.artisan.reader.CrawlingLocalDatePatterns;
import org.artisan.reader.CrawlingUtils;
import org.artisan.reader.PageItemReader;
import org.artisan.reader.TechBlogReader;
import org.artisan.util.fluent.cralwer.ContentsLoader;
import org.artisan.util.fluent.cralwer.ContentsReader;
import org.artisan.util.fluent.cralwer.Pages;
import org.artisan.util.fluent.cralwer.PagesInitializer;
import org.artisan.util.fluent.cralwer.PaginationReader;
import org.artisan.util.fluent.cralwer.PaginationReader.PaginationInformation;
import org.artisan.util.fluent.cralwer.SimpleContentsLoader;
import org.artisan.util.fluent.cralwer.WebDriverPool;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.util.regex.Pattern;
import org.openqa.selenium.By;


@Slf4j
@Configuration
public class SaraminCrawler {

    private static final String BASE_URL = "https://saramin.github.io/";
    private static final String PAGE_URL = "https://saramin.github.io/page";

    private static final TechBlogCode CODE = TechBlogCode.SARAMIN;

    @Bean
    public TechBlogReader saraminPages(WebDriverPool webDriverPool) {
        return new ProxyTechBlogReader(() -> {
            var page = Pages.<ExternalBlogPosts>builder()
                    .pagesInitializer(pagesInitializer())
                    .contentsLoader(contentsLoader())
                    .paginationReader(paginationReader())
                    .contentsReader(contentsReader())
                    .webDriver(webDriverPool.borrow())
                    .webDriverCleaner(webDriverPool::returnObject)
                    .after(externalBlogs -> log.info("read {}", externalBlogs.posts()))
                    .build();
            return new PageItemReader(page, CODE);
        }, CODE);

    }

    PagesInitializer pagesInitializer() {
        return pageNumber -> pageNumber == 1 ? BASE_URL : PAGE_URL + pageNumber;
    }

    ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(By.className("container"));
    }

    ContentsReader<ExternalBlogPosts> contentsReader() {
        return driver -> driver.findElements(By.tagName("article"))
                .stream()
                .map(this::find)
                .collect(collectingAndThen(toList(), ExternalBlogPosts::new));
    }

    ExternalBlogPost find(WebElement article) {
        var title = article.findElement(By.tagName("h2"));
        var postMeta = article.findElement(By.tagName("p"));
        var saraminPostMeta = SaraminPostMeta.parser(postMeta.getText().trim());
        var postEntry = article.findElement(By.className("post-entry"));
        var url = postEntry.findElement(By.tagName("a")).getAttribute("href");
        var summary = postEntry.getText().replaceAll("\\[Read More]", "");

        return ExternalBlogPost.builder()
                .code(CODE)
                .link(url)
                .suffix(url.substring(BASE_URL.length()))
                .summary(summary)
                .title(title.getText())
                .author(saraminPostMeta.author())
                .postDate(saraminPostMeta.localDate())
                .build();

    }

    PaginationReader paginationReader() {
        return driver -> {
            var pager = driver.findElement(By.className("pager"));

            if (!CrawlingUtils.existsByElement(() -> pager.findElement(By.className("next")))) {
                return PaginationInformation.stopInformation();
            }

            var maxPage = pager.findElements(By.className("next"))
                    .stream()
                    .map(next -> next.findElement(By.tagName("a"))
                            .getAttribute("href")
                            .substring(PAGE_URL.length()))
                    .mapToInt(Integer::parseInt)
                    .max()
                    .orElseThrow();

            return new PaginationInformation(maxPage);
        };
    }

    record SaraminPostMeta(String author, LocalDate localDate) {


        static final Pattern NAME_DATETIME_PATTERN = Pattern.compile("Posted by (.*) on (.*)");
        static final Pattern ONLY_DATETIME_PATTERN = Pattern.compile("Posted on (.*)");

        public static SaraminPostMeta parser(final String text) {
            var nameAndDateTimeMatcher = NAME_DATETIME_PATTERN.matcher(text);
            if (nameAndDateTimeMatcher.find()) {
                return new SaraminPostMeta(
                        nameAndDateTimeMatcher.group(1),
                        CrawlingLocalDatePatterns.PATTERN7.parse(nameAndDateTimeMatcher.group(2))
                );
            }

            var onlyDateTimeMatcher = ONLY_DATETIME_PATTERN.matcher(text);
            if (onlyDateTimeMatcher.find()) {
                return new SaraminPostMeta(
                        null,
                        CrawlingLocalDatePatterns.PATTERN7.parse(onlyDateTimeMatcher.group(1))
                );
            }
            throw new IllegalArgumentException();

        }

    }


}

