package org.artisan.util.fluent.cralwer;

import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;

@RequiredArgsConstructor
public class FluentWebDriverPoolFactory extends BasePooledObjectFactory<WebDriver> {

    private final DriverOptions driverOptions;

    @Override
    public WebDriver create() {
        return driverOptions.createWebDriver();
    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver driver) {
        return new DefaultPooledObject<>(driver);
    }

    @Override
    public void destroyObject(PooledObject<WebDriver> p, DestroyMode destroyMode) {
        p.getObject().quit();
    }
}

