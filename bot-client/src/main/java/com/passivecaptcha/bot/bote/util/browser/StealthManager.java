package com.passivecaptcha.bot.bote.util.browser;

import com.microsoft.playwright.BrowserContext;

public class StealthManager {

    public void apply(BrowserContext context) {

        context.addInitScript("""
            Object.defineProperty(navigator, 'webdriver', {
                get: () => undefined
            });

            Object.defineProperty(navigator, 'language', {
                get: () => navigator.language
            });

            Object.defineProperty(navigator, 'languages', {
                get: () => navigator.languages
            });

            Object.defineProperty(navigator, 'platform', {
                get: () => navigator.platform
            });

            Object.defineProperty(navigator, 'vendor', {
                get: () => navigator.vendor
            });

            window.chrome = window.chrome || {
                runtime: {}
            };
        """);
    }
}