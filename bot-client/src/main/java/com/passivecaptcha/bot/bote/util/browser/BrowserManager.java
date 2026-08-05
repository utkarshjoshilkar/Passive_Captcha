package com.passivecaptcha.bot.bote.util.browser;

import com.microsoft.playwright.*;

public class BrowserManager {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    private final ContextBuilder contextBuilder;
    private final StealthManager stealthManager;

    public BrowserManager() {

        contextBuilder = new ContextBuilder();
        stealthManager = new StealthManager();
    }

    public void launch(
            BrowserProfile browserProfile,
            FingerprintProfile fingerprintProfile
    ) {

        playwright = Playwright.create();

        browser = playwright.chromium().launch(

                new BrowserType.LaunchOptions()

                        .setHeadless(false)
        );

        Browser.NewContextOptions options =

                contextBuilder.build(
                        browserProfile,
                        fingerprintProfile
                );

        context = browser.newContext(options);

        stealthManager.apply(context);

        page = context.newPage();
    }

    public Page getPage() {

        return page;
    }

    public BrowserContext getContext() {

        return context;
    }

    public Browser getBrowser() {

        return browser;
    }

    public void close() {

        if (context != null)
            context.close();

        if (browser != null)
            browser.close();

        if (playwright != null)
            playwright.close();
    }
}