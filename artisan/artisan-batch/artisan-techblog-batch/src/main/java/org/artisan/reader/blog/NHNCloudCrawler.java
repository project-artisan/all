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
public class NHNCloudCrawler {

    private static final String PAGE_URL_FORMAT = "https://meetup.nhncloud.com/?page=%d";
    private static final TechBlogCode CODE = TechBlogCode.NHN_CLOUD;

    @Bean
    public TechBlogReader nhnCloudPages(WebDriverPool webDriverPool) {
        return new ProxyTechBlogReader(() -> {
            var page = Pages.<ExternalBlogPosts>builder()
                    .pagesInitializer(pagesInitializer())
                    .contentsLoader(contentsLoader())
                    .paginationReader(paginationReader())
                    .contentsReader(contentsReader())
                    .webDriver(webDriverPool.borrow())
                    .webDriverCleaner(webDriverPool::returnObject)
                    .after(externalBlogs -> log.info("NHN Cloud 크롤링 결과: {}", externalBlogs.posts()))
                    .build();
            return new PageItemReader(page, CODE);
        }, CODE);
    }

    PagesInitializer pagesInitializer() {
        return pageNumber -> String.format(PAGE_URL_FORMAT, pageNumber);
    }

    ContentsLoader contentsLoader() {
        // 'section_inner' 영역이 로드될 때까지 기다림
        return new SimpleContentsLoader(By.className("section_inner"));
    }

    ContentsReader<ExternalBlogPosts> contentsReader() {
        return driver -> {
            var container = driver.findElement(By.className("section_inner"));
            var cards = container.findElements(By.tagName("li"));
            return cards.stream()
                    .map(this::parseCard)
                    .collect(collectingAndThen(toList(), ExternalBlogPosts::new));
        };
    }

    ExternalBlogPost parseCard(WebElement card) {
        // 썸네일 URL 추출 (style 속성 예: "background-image: url('http://example.com/image.jpg');")
        var style = card.findElement(By.className("img_area")).getAttribute("style");
        String thumbnailUrl = extractUrlFromStyle(style);

        var titleElement = card.findElement(By.tagName("h3"));
        var linkElement = card.findElement(By.tagName("a"));
        var contentElement = card.findElement(By.tagName("p"));
        var dateElement = card.findElement(By.className("date"));
        // 날짜 텍스트를 공백으로 split한 후 두 번째 토큰을 파싱 (예: "Posted 2025-02-27" → "2025-02-27")
        var dateParts = dateElement.getText().split(" ");
        var postDate = CrawlingLocalDatePatterns.PATTERN1.parse(dateParts[1]);

        return ExternalBlogPost.builder()
                .thumbnailUrl(thumbnailUrl)
                .title(titleElement.getText())
                .code(CODE)
                .summary(contentElement.getText())
                .postDate(postDate)
                .suffix(linkElement.getAttribute("ng-href"))
                .link(linkElement.getAttribute("href"))
                .build();
    }

    String extractUrlFromStyle(String style) {
        // style 예: "background-image: url('http://example.com/image.jpg');"
        final String prefix = "background-image: url(";
        if (style != null && style.startsWith(prefix)) {
            String url = style.substring(prefix.length()).trim();
            // 앞뒤의 작은따옴표("'", 또는 "\"")와 닫는 문자 제거
            if (url.startsWith("'") || url.startsWith("\"")) {
                url = url.substring(1);
            }
            int endIndex = url.lastIndexOf("'");
            if (endIndex == -1) {
                endIndex = url.lastIndexOf("\"");
            }
            if (endIndex != -1) {
                url = url.substring(0, endIndex);
            }
            return url;
        }
        return "";
    }

    PaginationReader paginationReader() {
        return driver -> {
            // 'tui-pagination' 영역 내의 페이지 버튼들에서 최대 페이지 번호를 추출
            var paginationElement = driver.findElement(By.className("tui-pagination"));
            var buttons = paginationElement.findElements(By.className("tui-page-btn"));
            int maxPage = buttons.stream()
                    .map(WebElement::getText)
                    .filter(CrawlingUtils::isNumber)
                    .mapToInt(Integer::parseInt)
                    .max()
                    .orElse(1);
            return new PaginationInformation(maxPage);
        };
    }
}