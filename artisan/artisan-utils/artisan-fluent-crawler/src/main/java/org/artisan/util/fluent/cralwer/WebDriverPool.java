package org.artisan.util.fluent.cralwer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverPool extends GenericObjectPool<WebDriver> {
    private static final int MAX_TOTAL = 5;

    private final Set<WebDriver> remainObject = new HashSet<>();

    public WebDriverPool(PooledObjectFactory<WebDriver> factory) {
        super(factory);
    }

    public WebDriverPool(PooledObjectFactory<WebDriver> factory, int max) {
        super(factory, defaultConfig());
        this.setMaxTotal(max);
    }

    // jmx default 빈 등록 설정 막ㄱ
    private static GenericObjectPoolConfig<WebDriver> defaultConfig(){
        var config = new GenericObjectPoolConfig<WebDriver>();
        config.setJmxEnabled(false);
        return config;
    }

    public WebDriverPool maxTotal(int maxTotal) {
        this.setMaxTotal(maxTotal);
        return this;
    }


    public <T> T delegate(Function<WebDriver, T> function) {
        var webdriver = this.borrow();
        try {
            return function.apply(webdriver);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.returnObject(webdriver);
        }
    }

    public WebDriver borrow() {
        try {
            var webdriver = this.borrowObject();
            remainObject.add(webdriver);
            return webdriver;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void returnObject(WebDriver obj) {
        remainObject.remove(obj);
        super.returnObject(obj);
    }

    @Override
    public void close() {
        remainObject.forEach(super::returnObject);
        super.close();
    }

    @RequiredArgsConstructor
    public static class WebDriverPoolFactory extends BasePooledObjectFactory<WebDriver> {
        private final FirefoxOptions firefoxOptions;

        @Override
        public WebDriver create() {
            return new FirefoxDriver(firefoxOptions);
        }

        @Override
        public PooledObject<WebDriver> wrap(WebDriver webDriver) {
            return new DefaultPooledObject<>(webDriver);
        }

        @Override
        public void destroyObject(PooledObject<WebDriver> p, DestroyMode destroyMode) {
            p.getObject().quit();
        }


    }
}
