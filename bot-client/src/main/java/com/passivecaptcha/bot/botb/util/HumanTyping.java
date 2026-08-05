package com.passivecaptcha.bot.botb.util;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import com.passivecaptcha.bot.botb.util.SessionPersonality;
import java.util.concurrent.ThreadLocalRandom;

public class HumanTyping {

    private final Page page;

    private final SessionPersonality personality;

    public HumanTyping(Page page,
                   SessionPersonality personality) {

    this.page = page;
    this.personality = personality;
}

    public void type(String selector, String text) throws InterruptedException {

        Locator locator = page.locator(selector);

        locator.click();

        for (char c : text.toCharArray()) {

            locator.pressSequentially(String.valueOf(c));

            Thread.sleep(getTypingDelay(personality));
        }
    }
    private int getTypingDelay(SessionPersonality personality) {

    switch (personality) {

        case FAST:
            return ThreadLocalRandom.current().nextInt(20, 61);

        case NORMAL:
            return ThreadLocalRandom.current().nextInt(50, 121);

        case CAREFUL:
            return ThreadLocalRandom.current().nextInt(120, 221);

        case DISTRACTED:

            if (ThreadLocalRandom.current().nextInt(100) < 10) {
                return ThreadLocalRandom.current().nextInt(600, 1201);
            }

            return ThreadLocalRandom.current().nextInt(40, 151);

        default:
            return ThreadLocalRandom.current().nextInt(50, 121);
    }
}
}