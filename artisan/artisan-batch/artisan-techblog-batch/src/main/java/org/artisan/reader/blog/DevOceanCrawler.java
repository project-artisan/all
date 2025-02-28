package org.artisan.reader.blog;


import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
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
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.util.regex.Pattern;
import org.openqa.selenium.By;


@Slf4j
@Configuration
public class DevOceanCrawler {

    // 네이버의 경우 크롤링 페이지의 위치가 news, helloworld 이렇게 2개의 페이지가 존재하기 때문에 별도의 domain만 prefix를 가지도록 설정
    private static final String TARGET_URL = "https://devocean.sk.com/blog/index.do?ID=&boardType=&searchData=&searchDataMain=&page=%d&subIndex=&searchText=&techType=&searchDataSub=&comment=";
    private static final String PREFIX_URL = "https://devocean.sk.com/blog/techBoardDetail"
            + ".do?ID=165601&boardType=techBlog";

    private static final String URL_FORMAT = "https://devocean.sk.com/blog/techBoardDetail"
            + ".do?ID=%s";


    private static final TechBlogCode CODE = TechBlogCode.DEVOCEAN;

    @Bean
    public TechBlogReader devoceanCrawler(WebDriverPool webDriverPool) {
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
        return () -> new BasePageUrls(TARGET_URL, page -> String.format(TARGET_URL, page));
    }

    ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(By.className("sec-cont"));
    }

    ContentsReader<ExternalBlogPosts> contentsReader() {
        return driver -> driver.findElement(By.className("sec-area-list01"))
                .findElements(By.tagName("li"))
                .stream()
                .map(this::find)
                .collect(collectingAndThen(toList(), ExternalBlogPosts::new));
    }

    ExternalBlogPost find(WebElement article) {
        final var title = article.findElement(By.className("sec-cont")).findElement(By.className("title")).getText();
        final var description = article.findElement(By.className("desc"));
        final var author = article.findElement(By.className("author")).findElement(By.tagName("em"));
        final var date = CrawlingUtils.findByElement(() -> article.findElement(By.className("date")));
        final var img = article.findElement(By.className("sec-img")).findElement(By.tagName("img"));

        var postId = article.findElement(By.className("favorites")).getAttribute("data-board-id");

        return ExternalBlogPost.builder()
                .link(String.format(URL_FORMAT, postId))
                .suffix(postId)
                .title(title)
                .author(author.getText())// naver는 작성자가 없음
                .summary(description.getText())
                .thumbnailUrl(img.getAttribute("src"))
                .postDate(date.map(WebElement::getText)
                        .map(CrawlingLocalDatePatterns.PATTERN4::parse)
                        .orElse(null))
                .code(TechBlogCode.DEVOCEAN)
                .build();
    }

    PaginationReader paginationReader() {
        return driver -> PaginationInformation.lastPage(102);
    }
}