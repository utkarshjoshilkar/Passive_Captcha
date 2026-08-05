package com.passivecaptcha.bot.bote.util.browser;
import com.microsoft.playwright.options.ColorScheme;
import java.util.concurrent.ThreadLocalRandom;

public class BrowserProfileManager {

    private static final BrowserProfile[] BROWSER_PROFILES = {

            new BrowserProfile(
                    1920,
                    1080,
                    "en-US",
                    "America/New_York",
                    ColorScheme.LIGHT
            ),

            new BrowserProfile(
                    1366,
                    768,
                    "en-IN",
                    "Asia/Kolkata",
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
                    1600,
                    900,
                    "en-AU",
                    "Australia/Sydney",
                    ColorScheme.LIGHT
            ),

            new BrowserProfile(
                    1440,
                    900,
                    "en-CA",
                    "America/Toronto",
                    ColorScheme.DARK
            )
    };

    private static final FingerprintProfile[] FINGERPRINTS = {

            new FingerprintProfile(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36",
                    "Win32",
                    "Google Inc.",
                    8,
                    8,
                    new String[]{"en-US","en"}
            ),

            new FingerprintProfile(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36",
                    "Win32",
                    "Google Inc.",
                    4,
                    4,
                    new String[]{"en-IN","en"}
            ),

            new FingerprintProfile(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36",
                    "Win32",
                    "Google Inc.",
                    12,
                    16,
                    new String[]{"en-GB","en"}
            )
    };

    public BrowserProfile randomBrowserProfile() {

        return BROWSER_PROFILES[
                ThreadLocalRandom.current()
                        .nextInt(BROWSER_PROFILES.length)
        ];
    }

    public FingerprintProfile randomFingerprint() {

        return FINGERPRINTS[
                ThreadLocalRandom.current()
                        .nextInt(FINGERPRINTS.length)
        ];
    }
}