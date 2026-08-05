package com.passivecaptcha.bot.botb.util;

import com.microsoft.playwright.Browser;

public class DefaultEnvironment implements BrowserEnvironment {

    @Override
    public Browser.NewContextOptions configure() {

        return new Browser.NewContextOptions()
                .setViewportSize(1366, 768);

    }
}