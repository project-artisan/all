package org.artisan.reader.blog;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.stream.Collectors;
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
public class DevSistersCrawler {

    private static final String TARGET_URL = "https://tech.devsisters.com/";
    private static final TechBlogCode CODE = TechBlogCode.DEV_SISTERS;

    @Bean
    public TechBlogReader devSistersPages(WebDriverPool webDriverPool) {
        return new ProxyTechBlogReader(() -> {
            var page = Pages.<ExternalBlogPosts>builder()
                    .pagesInitializer(pagesInitializer())
                    .contentsLoader(contentsLoader())
                    .paginationReader(paginationReader())
                    .contentsReader(contentsReader())
                    .webDriver(webDriverPool.borrow())
                    .webDriverCleaner(webDriverPool::returnObject)
                    .after(externalBlogs -> log.info("DevSisters 크롤링 결과: {}", externalBlogs.posts()))
                    .build();
            return new PageItemReader(page, CODE);
        }, CODE);
    }

    PagesInitializer pagesInitializer() {
        // 페이지 URL 생성: TARGET_URL + "?page=" + pageNumber
        return pageNumber -> TARGET_URL + "?page=" + pageNumber;
    }

    ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(By.cssSelector(".container-layout"));
//        return new SimpleContentsLoader(By.cssSelector("ul.container-layout"));
    }

    ContentsReader<ExternalBlogPosts> contentsReader() {
        return driver -> {
            var articles = driver.findElements(By.className("group/item"));
            return articles.stream()
                    .map(e -> e.findElement(By.tagName("a")))
                    .map(this::parseArticle)
                    .collect(collectingAndThen(Collectors.toList(), ExternalBlogPosts::new));
        };
    }

    ExternalBlogPost parseArticle(WebElement article) {
        String href = article.getAttribute("href");
        String title = article.findElement(By.cssSelector("h3")).getText();
        String summary = article.findElement(By.cssSelector("p")).getText();
        String dateText = article.findElement(By.tagName("time")).getText();
        String author = article.findElement(By.cssSelector("span.font-medium")).getText();
        String thumbnailUrl = article.findElement(By.cssSelector("div picture img")).getAttribute("src");

        return ExternalBlogPost.builder()
                // href가 상대 경로일 경우 BASE_URL과 결합해 절대 URL로 변경
                .link(href.startsWith("http") ? href : TARGET_URL+ href)
                .suffix(href)  // 필요에 따라 상대 경로만 남기거나 가공
                .title(title)
                .summary(summary)
                .postDate(CrawlingLocalDatePatterns.PATTERN5.parse(dateText))
                .author(author)
                .thumbnailUrl(thumbnailUrl)
                .code(CODE)
                .build();
    }

    PaginationReader paginationReader() {
        return driver -> {
//            // 지정된 Pagination 영역이 로드될 때까지 대기
//            driver.findElement(By.className("Pagination-module--list--1OcIo"));
//            int maxPage = driver.findElement(By.className("Pagination-module--list--1OcIo"))
//                    .findElements(By.tagName("span"))
//                    .stream()
//                    .map(WebElement::getText)
//                    .filter(text -> {
//                        try {
//                            Integer.parseInt(text);
//                            return true;
//                        } catch (NumberFormatException ex) {
//                            return false;
//                        }
//                    })
//                    .mapToInt(Integer::parseInt)
//                    .max()
//                    .orElse(1);
            return PaginationInformation.lastPage(4);
        };
    }
}
