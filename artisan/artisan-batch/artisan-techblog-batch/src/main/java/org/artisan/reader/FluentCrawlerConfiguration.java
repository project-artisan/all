package org.artisan.reader;


import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import java.util.Map;

import java.util.List;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.artisan.core.TechBlogCode;
import org.artisan.util.fluent.cralwer.DriverOptions;
import org.artisan.util.fluent.cralwer.FluentWebDriverPoolFactory;
import org.artisan.util.fluent.cralwer.WebDriverPool;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FluentCrawlerConfiguration {

    @Bean
    Map<TechBlogCode, TechBlogReader> fluentPageItemReaders(List<TechBlogReader> pageItemReaders) {
        return pageItemReaders.stream()
                .collect(toMap(
                        TechBlogReader::getTechBlogCode,
                        identity()
                ));

    }

    @Bean(destroyMethod = "close", name ="webDriverPool")
    WebDriverPool webDriverPool() {
        return new WebDriverPool(webDriverPoolFactory(new DriverOptions("", "local", new String[]{
                "--headless",
                "--disable-gpu",
                "--disable-extensions",
                "--disable-infobars",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--blink-settings=imagesEnabled=false"
        })), 10);
    }

    private BasePooledObjectFactory<WebDriver> webDriverPoolFactory(DriverOptions options) {
        return new FluentWebDriverPoolFactory(options);
    }


}

