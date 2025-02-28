package org.artisan.reader.blog;


import static java.util.stream.Collectors.collectingAndThen;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPost;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.reader.CrawlingLocalDatePatterns;
import org.artisan.reader.CrawlingUtils;
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
import org.springframework.context.annotation.Configuration;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Bean;


@Slf4j
@Configuration
@RequiredArgsConstructor
//TODO 새로운 유형의 블로그
public class KakaoCrawler {

    private static final String BASE_URL = "https://tech.kakao.com/blog/";
    private static final String PAGE_URL = "https://tech.kakao.com/blog/page/";
    private static final String PREFIX = "https://tech.kakao.com/";
    private static final TechBlogCode CODE = TechBlogCode.KAKAO;


    @Bean
    TechBlogReader kakaoReader(WebDriverPool webDriverPool) {
        var pages = GenericParallelPages.<ExternalBlogPosts>builder()
                .webDriverPool(webDriverPool)
                .pageInitializer(pageInitializer())
                .contentsLoader(contentsLoader())
                .paginationReader(paginationReader())
                .parallelCount(3)
                .contentsReader(contentsReader())
                .after(data -> log.info("{}", data))
                .build();
        return new ParallelPageItemReader(pages, CODE);
    }

    private ContentsReader<ExternalBlogPosts> contentsReader() {
        return webDriver -> webDriver.findElement(className("elementor-element-150ab43d"))
                .findElements(By.tagName("article"))
                .stream().map(this::parse)
                .collect(collectingAndThen(Collectors.toList(), ExternalBlogPosts::new));
    }

    private ExternalBlogPost parse(WebElement webElement) {
        var titleElement = webElement.findElement(cssSelector("h3 a"));
        var authorElement = webElement.findElement(className("elementor-post-author"));
        var postDateElement = webElement.findElement(className("elementor-post-date"));
        var summaryElement = webElement.findElement(className("elementor-post__excerpt"));

        var link = titleElement.getAttribute("href");
        var dateText = postDateElement.getText()
                .trim()
                .replaceAll("///", "");

        return ExternalBlogPost.builder()
                .code(CODE)
                .link(link)
                .title(titleElement.getText())
                .suffix(link.substring(PREFIX.length()))
                .author(authorElement.getText())
                .postDate(CrawlingLocalDatePatterns.PATTERN1.parse(dateText))
                .summary(summaryElement.getText())
                .build();
    }

    private PaginationReader paginationReader() {
        return webDriver -> PaginationInformation.lastPage(webDriver.findElement(className("elementor-pagination"))
                .findElements(By.tagName("a"))
                .stream()
                .map(WebElement::getText)
                .map(page -> page.replaceAll("Page", ""))
                .filter(CrawlingUtils::isNumber)
                .map(Integer::parseInt)
                .reduce(Integer.MIN_VALUE, Math::max));

    }

    private ParallelPageInitializer pageInitializer() {
        return () -> new BasePageUrls(
                BASE_URL,
                pageNumber -> {
                    log.info("{}", PAGE_URL + pageNumber);
                    return PAGE_URL + pageNumber;
                }
        );
    }

    private ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(className("elementor-posts"));
    }

}
