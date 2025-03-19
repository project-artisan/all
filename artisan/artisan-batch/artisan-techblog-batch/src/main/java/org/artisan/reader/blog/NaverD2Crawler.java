package org.artisan.reader.blog;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.openqa.selenium.By;

@Slf4j
@Configuration
public class NaverD2Crawler {

    // 네이버의 경우 크롤링 페이지의 위치가 news, helloworld 이렇게 2개의 페이지가 존재하기 때문에 별도의 domain만 prefix를 가지도록 설정
    private static final String TARGET_URL = "https://d2.naver.com/home";
    private static final String PREFIX_URL = "https://d2.naver.com/";

    private static final TechBlogCode CODE = TechBlogCode.NAVER;

    @Bean
    public TechBlogReader naverD2Pages(WebDriverPool webDriverPool) {
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
        return () -> new BasePageUrls(TARGET_URL, page -> TARGET_URL + "?page=" + page);
    }

    ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(By.className("post_article"));
    }

    ContentsReader<ExternalBlogPosts> contentsReader() {
        return driver -> driver.findElements(By.className("post_article"))
                .stream()
                .map(this::find)
                .collect(collectingAndThen(toList(), ExternalBlogPosts::new));
    }

    ExternalBlogPost find(WebElement article) {
        final var postElement = article.findElement(By.className("cont_post"));

        final var titleElement = postElement.findElement(By.tagName("h2"))
                .findElement(By.tagName("a"));
        // naver post title
        final var url = titleElement.getAttribute("href");
        final var title = titleElement.getText();

        // naver post body
        final var thumbnail = postElement.findElement(By.className("cont_img"))
                .findElement(By.tagName("img"))
                .getAttribute("src");
        final var summary = postElement.findElement(By.className("post_txt"))
                .getText();
        final var postDate = postElement.findElement(By.tagName("dl"))
                .findElement(By.tagName("dd"))
                .getText();

        return ExternalBlogPost.builder()
                .link(url)
                .suffix(url.substring(PREFIX_URL.length()))
                .title(title)
                .author("") // naver는 작성자가 없음
                .summary(summary)
                .thumbnailUrl(thumbnail)
                .postDate(CrawlingLocalDatePatterns.PATTERN1.parse(postDate))
                .code(CODE)
                .build();
    }

    PaginationReader paginationReader() {
        return driver -> {
            var maxPage = driver.findElements(By.className("btn_num"))
                    .stream()
                    .map(webElement -> webElement.getAttribute("data-number"))
                    .mapToInt(Integer::parseInt)
                    .max()
                    .orElseThrow(IllegalArgumentException::new);
            return PaginationInformation.lastPage(33);
        };
    }
}