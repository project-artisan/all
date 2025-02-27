package org.artisan.util.fluent.cralwer;



import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public record DriverOptions (
        String url,
        String type,
        String[] options
) {
    public boolean isRemote() {
        return Objects.equals(type, "remote");
    }

    public FirefoxOptions getOptions() {
        return new FirefoxOptions()
                .setPageLoadStrategy(PageLoadStrategy.EAGER)
                .addArguments(Arrays.asList(options));
    }

    public URL getUrl() {
        try {
            return new URL(this.url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public WebDriver createWebDriver() {
        if (isRemote()) {
            return new RemoteWebDriver(getUrl(), getOptions());
        }

        return new FirefoxDriver(getOptions());
    }
}
