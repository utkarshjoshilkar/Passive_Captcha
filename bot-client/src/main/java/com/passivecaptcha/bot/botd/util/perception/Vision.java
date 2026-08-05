package com.passivecaptcha.bot.botd.util.perception;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class Vision {

    private final Page page;

    public Vision(Page page) {
        this.page = page;
    }

    public boolean isVisible(String selector) {

        Locator locator = page.locator(selector);

        return locator.isVisible();
    }

    public boolean exists(String selector) {

        return page.locator(selector).count() > 0;
    }

    public boolean isEnabled(String selector) {

        Locator locator = page.locator(selector);

        return locator.isEnabled();
    }

    public boolean isEditable(String selector) {

        Locator locator = page.locator(selector);

        return locator.isEditable();
    }

    public String currentUrl() {

        return page.url();
    }

}