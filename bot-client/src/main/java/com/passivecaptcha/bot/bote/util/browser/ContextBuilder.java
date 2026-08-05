package com.passivecaptcha.bot.bote.util.browser;

import com.microsoft.playwright.Browser;

public class ContextBuilder {

    public Browser.NewContextOptions build(
            BrowserProfile browserProfile,
            FingerprintProfile fingerprintProfile
    ) {

        Browser.NewContextOptions options =
                new Browser.NewContextOptions();

        options.setViewportSize(
                browserProfile.getWidth(),
                browserProfile.getHeight()
        );

        options.setLocale(
                browserProfile.getLocale()
        );

        options.setTimezoneId(
                browserProfile.getTimezone()
        );

        options.setColorScheme(
                browserProfile.getColorScheme()
        );

        options.setUserAgent(
                fingerprintProfile.getUserAgent()
        );

        return options;
    }

}