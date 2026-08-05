package com.passivecaptcha.bot.botb.util;

import java.util.concurrent.ThreadLocalRandom;
import com.microsoft.playwright.options.ColorScheme;

public class VariantEnvironment {

    private static final BrowserProfile[] PROFILES = {

            new BrowserProfile(
                    1366,
                    768,
                    "en-IN",
                    "Asia/Kolkata",
                    ColorScheme.LIGHT
            ),

            new BrowserProfile(
                    1920,
                    1080,
                    "en-US",
                    "America/New_York",
                    ColorScheme.LIGHT
            ),

            new BrowserProfile(
                    1536,
                    864,
                    "en-GB",
                    "Europe/London",
                    ColorScheme.DARK
            ),

            new BrowserProfile(
                    1440,
                    900,
                    "en-AU",
                    "Australia/Sydney",
                    ColorScheme.LIGHT
            ),

            new BrowserProfile(
                    2560,
                    1440,
                    "en-US",
                    "America/Chicago",
                    ColorScheme.DARK
            )

    };
    public static BrowserProfile randomProfile() {

        int index = ThreadLocalRandom.current()
                .nextInt(PROFILES.length);

        return PROFILES[index];

    }

}