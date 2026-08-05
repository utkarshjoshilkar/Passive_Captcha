package com.passivecaptcha.bot.bota.util;

import org.openqa.selenium.WebDriver;

public class DefaultEnvironment implements BrowserEnvironment {

    @Override
    public void configure(WebDriver driver) {

        driver.manage().window().maximize();

    }

}