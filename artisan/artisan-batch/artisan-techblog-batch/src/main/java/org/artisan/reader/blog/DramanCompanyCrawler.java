package org.artisan.reader.blog;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.ExternalBlogPost;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.proxy.ProxyTechBlogReader;
import org.artisan.reader.CrawlingUtils;
import org.artisan.reader.PageItemReader;
import org.artisan.reader.TechBlogReader;
import org.artisan.util.fluent.cralwer.ContentsLoader;
import org.artisan.util.fluent.cralwer.ContentsReader;
import org.artisan.util.fluent.cralwer.SimpleContentsLoader;
import org.artisan.util.fluent.cralwer.SinglePage;
import org.artisan.util.fluent.cralwer.WebDriverPool;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DramanCompanyCrawler {

    private static final String BASE_URL = "https://tech.remember.co.kr/";
    private static final TechBlogCode CODE = TechBlogCode.DRAMANCOMPANY;

    @Bean
    public TechBlogReader dramanCompayPages(WebDriverPool webDriverPool) {
        return new ProxyTechBlogReader(() -> {
            var page = SinglePage.<ExternalBlogPosts>builder()
                    .singlePageInitializer(() -> BASE_URL)
                    .contentsLoader(contentsLoader())
                    .contentsReader(contentsReader())
                    .after((e) -> log.info("{}", e))
                    .webDriver(webDriverPool.borrow())
                    .webDriverCleaner(webDriverPool::returnObject)
                    .build();
            return new PageItemReader(page, CODE);
        }, CODE);
    }
    // 포스트 리스트가 포함된 외부 컨테이너(예, div.n.p)를 로드할 때까지 기다림
    ContentsLoader contentsLoader() {
        return new SimpleContentsLoader(By.cssSelector("div.n.p"));
    }


    // 페이지 내의 모든 게시글(article 태그)을 ExternalBlogPosts로 변환
    ContentsReader<ExternalBlogPosts> contentsReader() {
        return driver -> {

            CrawlingUtils.renderAll(driver);
            // article 태그가 각 포스트를 감싸고 있음
            List<WebElement> articles = driver.findElements(By.tagName("article"));
            List<ExternalBlogPost> posts = articles.stream()
                    .map(this::parseArticle)
                    .collect(Collectors.toList());
            return new ExternalBlogPosts(posts);
        };
    }

    // 각 게시글(article)에서 필요한 정보를 추출하는 메서드
    ExternalBlogPost parseArticle(WebElement article) {
        // 1. 링크: role="link" 속성이 있는 div의 data-href 값을 사용
        String link = "";
        try {
            WebElement linkContainer = article.findElement(By.cssSelector("div[role='link']"));
            link = linkContainer.getAttribute("data-href");
        } catch (Exception e) {
            log.error("링크 추출 실패", e);
        }

        // 2. 제목: 게시글 내의 h2 태그 텍스트 (포스트 제목)
        String title = "";
        try {
            WebElement titleElement = article.findElement(By.cssSelector("h2"));
            title = titleElement.getText();
        } catch (Exception e) {
            log.warn("제목 추출 실패", e);
        }

        // 3. 요약: h3 태그에 있는 부제 또는 요약 텍스트
        String summary = "";
        try {
            WebElement summaryElement = article.findElement(By.cssSelector("h3"));
            summary = summaryElement.getText();
        } catch (Exception e) {
            log.warn("요약 추출 실패", e);
        }

        // 4. 날짜: 게시글 내 날짜는 대개 날짜 정보를 담은 span 요소에 있음 (예: "Nov 15, 2022")
        String dateText = "";
        try {
            // date는 article 내부의 특정 컨테이너에 포함되어 있으므로 적절한 선택자 사용
            WebElement dateElement = article.findElement(By.cssSelector("div.n.o.kx span"));
            dateText = dateElement.getText();
        } catch (Exception e) {
            log.warn("날짜 추출 실패", e);
        }
        // 날짜 파싱 – CrawlingLocalDatePatterns에 PATTERN_MEDIUM("MMM d, yyyy")가 등록되어 있다고 가정
//        LocalDate postDate = CrawlingLocalDatePatterns.PATTERN_MEDIUM.parse(dateText);
        // TODO 날짜 처리
        LocalDate postDate = LocalDate.now();

        // 5. 작성자: 작성자 정보는 보통 상단의 작성자 프로필 영역에 위치 (예, "Remember tech")
        String author = "";
        try {
            WebElement authorElement = article.findElement(By.cssSelector("a[href^='https://medium.com/@remember-tech'] p"));
            author = authorElement.getText();
        } catch (Exception e) {
            log.warn("작성자 추출 실패", e);
        }

        // 6. 썸네일: 메인 이미지 (게시글 내 img 태그 중, 작성자 프로필 이미지를 제외한 이미지)
        String thumbnailUrl = "";
        try {
            // 모든 img 태그 중, alt 속성이 게시글 제목과 관련된(또는 길이가 0이 아닌) 이미지를 선택
            List<WebElement> imgs = article.findElements(By.tagName("img"));
            for (WebElement img : imgs) {
                String alt = img.getAttribute("alt");
                // 작성자 프로필 이미지(예: "Remember tech")는 제외
                if (alt != null && !alt.contains("Remember tech") && !alt.isEmpty()) {
                    thumbnailUrl = img.getAttribute("src");
                    break;
                }
            }
        } catch (Exception e) {
            log.warn("썸네일 추출 실패", e);
        }

        return ExternalBlogPost.builder()
                .link(link.startsWith("http") ? link : BASE_URL + link)
                .suffix(link.replace(BASE_URL, ""))
                .title(title)
                .summary(summary)
                .postDate(postDate)
                .author(author)
                .thumbnailUrl(thumbnailUrl)
                .code(CODE)
                .build();
    }

}
