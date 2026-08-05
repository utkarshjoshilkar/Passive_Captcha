package com.passivecaptcha.bot.botb.util;

import com.microsoft.playwright.Browser;

public interface BrowserEnvironment {

    Browser.NewContextOptions configure();

}