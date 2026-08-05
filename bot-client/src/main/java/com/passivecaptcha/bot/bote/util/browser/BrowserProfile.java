package com.passivecaptcha.bot.bote.util.browser;

import com.microsoft.playwright.options.ColorScheme;

public class BrowserProfile {

    private final int width;
    private final int height;

    private final String locale;
    private final String timezone;

    private final ColorScheme colorScheme;

    public BrowserProfile(
            int width,
            int height,
            String locale,
            String timezone,
            ColorScheme colorScheme
    ) {

        this.width = width;
        this.height = height;
        this.locale = locale;
        this.timezone = timezone;
        this.colorScheme = colorScheme;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getLocale() {
        return locale;
    }

    public String getTimezone() {
        return timezone;
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }
}