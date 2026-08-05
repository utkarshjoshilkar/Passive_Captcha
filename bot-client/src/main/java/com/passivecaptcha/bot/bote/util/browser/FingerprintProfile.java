package com.passivecaptcha.bot.bote.util.browser;

public class FingerprintProfile {

    private final String userAgent;
    private final String platform;
    private final String vendor;
    private final int hardwareConcurrency;
    private final int deviceMemory;
    private final String[] languages;

    public FingerprintProfile(
            String userAgent,
            String platform,
            String vendor,
            int hardwareConcurrency,
            int deviceMemory,
            String[] languages
    ) {

        this.userAgent = userAgent;
        this.platform = platform;
        this.vendor = vendor;
        this.hardwareConcurrency = hardwareConcurrency;
        this.deviceMemory = deviceMemory;
        this.languages = languages;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getPlatform() {
        return platform;
    }

    public String getVendor() {
        return vendor;
    }

    public int getHardwareConcurrency() {
        return hardwareConcurrency;
    }

    public int getDeviceMemory() {
        return deviceMemory;
    }

    public String[] getLanguages() {
        return languages;
    }
}