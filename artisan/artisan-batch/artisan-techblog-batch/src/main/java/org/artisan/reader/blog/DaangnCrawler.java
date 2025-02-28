package org.artisan.reader.blog;


import static java.util.stream.Collectors.collectingAndThen;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPost;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.proxy.ProxyTechBlogReader;
import org.artisan.reader.CrawlingLocalDatePatterns;
import org.artisan.reader.CrawlingUtils;
import org.artisan.reader.PageItemReader;
import org.artisan.reader.ParallelPageItemReader;
import org.artisan.reader.TechBlogReader;
import org.artisan.util.fluent.cralwer.ContentsLoader;
import org.artisan.util.fluent.cralwer.ContentsReader;
import org.artisan.util.fluent.cralwer.GenericParallelPages;
import org.artisan.util.fluent.cralwer.Pages;
import org.artisan.util.fluent.cralwer.PagesInitializer;
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
//TODO 새로운 유형의 블로그 아 그냥 단일 크롤링 페이지 구현 할 것
public class DaangnCrawler {

    private static final String BASE_URL = "https://medium.com/daangn/archive/%d";
    private static final TechBlogCode CODE = TechBlogCode.DAANGN;

    @Bean
    public TechBlogReader daangnPages(WebDriverPool webDriverPool) {
        return new ProxyTechBlogReader(() -> {
            var page = GenericParallelPages.<ExternalBlogPosts>builder()
                    .pageInitializer(pagesInitializer())
                    .contentsLoader(contentsLoader())
                    .paginationReader(paginationReader())
                    .contentsReader(contentsReader())
                    .parallelCount(10)
                    .webDriverPool(webDriverPool)
                    .after(externalBlogs -> log.info("Medium 크롤링 결과: {}", externalBlogs.posts()))
                    .build();
            return new ParallelPageItemReader(page, CODE);
        }, CODE);
    }

    // 페이지 URL 생성 (필요에 따라 변경)
    ParallelPageInitializer pagesInitializer() {
        return () -> new BasePageUrls(String.format(BASE_URL, 2015), page -> String.format(BASE_URL, page));
    }

    // js-postStream 영역이 로드될 때까지 대기
    ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(By.cssSelector("body"));
    }

    // 포스트 스트림 내의 각 포스트 요소들을 ExternalBlogPost로 변환
    ContentsReader<ExternalBlogPosts> contentsReader() {
        return driver -> {
            var postElements = driver.findElements(By.cssSelector(".postArticle"));
            return postElements.stream()
                    .map(this::parsePost)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), ExternalBlogPosts::new));
        };
    }

    // 각 포스트에서 필요한 정보를 추출
    ExternalBlogPost parsePost(WebElement postElement) {
        // 링크: 포스트를 감싸는 a 태그의 href 속성
        WebElement anchor = postElement.findElement(By.cssSelector("a"));
        var link = anchor.getAttribute("href");

        WebElement titleElement = postElement.findElement(By.cssSelector("h3.graf--title"));
        var title = titleElement.getText();

        WebElement timeElement = postElement.findElement(By.cssSelector("time"));
        var dateText = timeElement.getText();
        LocalDate postDate = CrawlingLocalDatePatterns.PATTERN6.parse(dateText);
        WebElement authorElement = postElement.findElement(By.cssSelector("div.postMetaInline-authorLockup a"));
        var author = authorElement.getText();


        var thumbnailUrl = CrawlingUtils.findByElement(() -> postElement.findElement(By.cssSelector("img")))
                .map(e -> e.getAttribute("src"))
                .orElse("");

        return ExternalBlogPost.builder()
                .link(link.startsWith("http") ? link : BASE_URL + link)
                .suffix(link.replace(BASE_URL, ""))
                .title(title)
                .postDate(postDate)
                .author(author)
                .thumbnailUrl(thumbnailUrl)
                .code(CODE)
                .build();
    }

    // 페이지네이션 정보가 별도로 없다면 종료 정보를 반환
    PaginationReader paginationReader() {
        return driver -> new PaginationInformation(2015, Optional.of(2025));
    }
}
