package org.artisan.util.fluent.cralwer;

import org.artisan.util.fluent.cralwer.PaginationReader.PaginationInformation;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Pages<T> implements Page<T> {

    private final PagesInitializer pagesInitializer;
    private final ContentsLoader contentsLoader;
    private final ContentsReader<T> contentsReader;
    private final PaginationReader paginationReader;
    private final PagesStopper pagesStopper;
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;
    private final WebDriverCleaner webDriverCleaner;
    private final After<T> after;


    private int currentPage = 1;
    private PaginationInformation paginationInformation = null;

    @Builder
    public Pages(
            PagesInitializer pagesInitializer,
            ContentsReader<T> contentsReader,
            PaginationReader paginationReader,
            ContentsLoader contentsLoader,
            PagesStopper pagesStopper,
            WebDriverCleaner webDriverCleaner,
            After<T> after,
            WebDriver webDriver

    ) {
        Objects.requireNonNull(pagesInitializer);
        Objects.requireNonNull(contentsReader);
        Objects.requireNonNull(paginationReader);
        Objects.requireNonNull(webDriver);
        Objects.requireNonNull(contentsLoader);
        Objects.requireNonNull(webDriverCleaner);

        this.pagesInitializer = pagesInitializer;
        this.contentsReader = contentsReader;
        this.paginationReader = paginationReader;
        this.contentsLoader = contentsLoader;
        this.pagesStopper = pagesStopper;
        this.webDriver = webDriver;
        this.after = after;
        this.webDriverCleaner = webDriverCleaner;
        this.webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(100));
    }

    @Override
    public T execute() {
        return Optional.ofNullable(subExecute())
                .orElseGet(() -> cleanup(webDriver));
    }

    private T subExecute() {
        if (Objects.nonNull(paginationInformation) && paginationInformation.complete(currentPage)) {
            webDriverCleaner.cleanup(webDriver);
            return null;
        }
        webDriver.get(pagesInitializer.getUrl(currentPage));

        if (Objects.nonNull(pagesStopper) && pagesStopper.isSatisfy(webDriver, webDriverWait)) {
            webDriverCleaner.cleanup(webDriver);
            return null;
        }

        contentsLoader.waitUntilLoad(webDriverWait);

        paginationInformation = paginationReader.read(webDriver);

        currentPage++;

        final var result = contentsReader.read(webDriver);

        if (Objects.nonNull(after)) {
            after.action(result);
        }

        return result;
    }

}
