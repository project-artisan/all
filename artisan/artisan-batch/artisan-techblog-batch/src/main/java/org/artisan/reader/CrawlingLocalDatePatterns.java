package org.artisan.reader;


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.batch.item.ItemReader;


@RequiredArgsConstructor
public enum CrawlingLocalDatePatterns {
    PATTERN1("yyyy.MM.dd"),
    PATTERN2("yyyy.MM.dd."),
    PATTERN3("MMM.d.yyyy", Locale.ENGLISH),
    PATTERN4("yy.MM.dd"),
    PATTERN5("yyyy-MM-dd"),
    PATTERN6("MMM d, yyyy", Locale.ENGLISH),
    PATTERN7("MMMM d, yyyy", Locale.ENGLISH),
    PATTERN8("yyyy년 M월 d일", Locale.KOREAN),
    PATTERN9("yyyy.M.d"),
    PATTERN10(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
    PATTERN11("yyyy.M.d.");


    private final DateTimeFormatter dateTimeFormatter;

    CrawlingLocalDatePatterns(String pattern) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    CrawlingLocalDatePatterns(String pattern, Locale language) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, language);
    }

    public LocalDate parse(String text) {
        return LocalDate.parse(text, this.dateTimeFormatter);
    }

}
