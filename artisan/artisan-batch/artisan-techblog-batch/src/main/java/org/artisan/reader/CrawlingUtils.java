package org.artisan.reader;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Supplier;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CrawlingUtils {
    public static boolean isNumber(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public static Optional<WebElement> findByElement(Supplier<WebElement> webElementSupplier) {
        try {
            return Optional.ofNullable(webElementSupplier.get());
        } catch (NoSuchElementException noSuchElementException) {
            return Optional.empty();
        }
    }

    public static boolean existsByElement(Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (NoSuchElementException noSuchElementException) {
            return false;
        }
    }

    // img 제거
    public static String removeImgBracket(String input) {
        return input.replace("url(", "")
                .replace(")", "")
                .replace("\"", "");
    }

    public static String urlDecode(String url) {
        try {
            return URLDecoder.decode(url, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static String removeParameterWithUrl(String url) {
        return url.substring(0, url.indexOf("?"));

    }

    public static void renderAll(WebDriver webDriver) {

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
        // 페이지 맨 아래로 스크롤
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");


        while (true) {
            // 페이지 맨 아래로 스크롤
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            long newHeight = (long) js.executeScript("return document.body.scrollHeight");

            if (newHeight == lastHeight) {
                break;
            }
            lastHeight = newHeight;
        }
    }
}