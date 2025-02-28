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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class LineCrawler {

    private static final String TARGET_URL = "https://techblog.lycorp.co.jp/ko";
    private static final TechBlogCode CODE = TechBlogCode.LINE;

    @Bean
    public TechBlogReader linePages(WebDriverPool webDriverPool) {
        return new ProxyTechBlogReader(() -> {
            var page = Pages.<ExternalBlogPosts>builder()
                    .pagesInitializer(pagesInitializer())
                    .contentsLoader(contentsLoader())
                    .paginationReader(paginationReader())
                    .contentsReader(contentsReader())
                    .webDriver(webDriverPool.borrow())
                    .webDriverCleaner(webDriverPool::returnObject)
                    .after(externalBlogs -> log.info("Line 크롤링 결과: {}", externalBlogs.posts()))
                    .build();
            return new PageItemReader(page, CODE);
        }, CODE);
    }

    PagesInitializer pagesInitializer() {
        return pageNumber -> {
            if(pageNumber == 1) {
                return TARGET_URL;
            }
            return TARGET_URL + "/page/" + pageNumber;
        };
    }

    ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(By.className("list_post"));
    }

    ContentsReader<ExternalBlogPosts> contentsReader() {
        return driver -> {
            var listPost = driver.findElement(By.className("list_post"));
            var posts = listPost.findElements(By.tagName("li"));
            return posts.stream()
                    .map(this::parsePost)
                    .collect(collectingAndThen(toList(), ExternalBlogPosts::new));
        };
    }

    ExternalBlogPost parsePost(WebElement webElement) {
        // href 추출
        var aTag = webElement.findElement(By.tagName("a"));
        final var href = aTag.getAttribute("href");

        // 썸네일 이미지 추출
        final var image = webElement.findElement(By.className("thumbnail"))
                .findElement(By.tagName("img"))
                .getAttribute("src");

        // 제목 추출
        final var title = webElement.findElement(By.tagName("h2")).getText();

        // 날짜 추출 및 포맷팅 ("Date:" 문자열 및 개행 제거)
        final var dateText = webElement.findElement(By.className("update")).getText()
                .replace("Date:", "")
                .replace("\n", "")
                .trim();

        var postDate = CrawlingLocalDatePatterns.PATTERN1.parse(dateText);

        return ExternalBlogPost.builder()
                .title(title)
                .thumbnailUrl(image)
                .suffix(href.substring(TARGET_URL.length()))
                .code(CODE)
                .postDate(postDate)
                .link(href)
                .build();
    }

    PaginationReader paginationReader() {
        return driver -> {
            // "pagination" 영역이 나타날 때까지 대기
            driver.findElement(By.className("pagination"));
            // 여러 pagination 요소가 있을 경우, 각 페이지 번호를 포함하는 "page" 클래스를 가진 요소의 텍스트를 숫자로 변환하여 최대값 계산
            int maxPage = driver.findElements(By.className("pagination"))
                    .stream()
                    .map(pageContainer -> pageContainer.findElement(By.className("page")))
                    .map(WebElement::getText)
                    .filter(CrawlingUtils::isNumber)
                    .mapToInt(Integer::parseInt)
                    .max()
                    .orElseThrow();
            return PaginationInformation.lastPage(8);
        };
    }
}
