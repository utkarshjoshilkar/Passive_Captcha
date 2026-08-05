package com.passivecaptcha.bot.botb.util;
import java.util.concurrent.ThreadLocalRandom;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HumanMouse {

    private final Page page;
private final SessionPersonality personality;

    public HumanMouse(Page page, SessionPersonality personality) {
        this.page = page;
        this.personality = personality;
    }

    public void moveToElement(String selector) throws InterruptedException {

        Locator locator = page.locator(selector);

        locator.scrollIntoViewIfNeeded();

        locator.hover();

        Thread.sleep(getHoverDelay());
    }
    private int getHoverDelay() {

    switch (personality) {

        case FAST:
            return ThreadLocalRandom.current().nextInt(50, 151);

        case NORMAL:
            return ThreadLocalRandom.current().nextInt(150, 301);

        case CAREFUL:
            return ThreadLocalRandom.current().nextInt(300, 701);

        case DISTRACTED:

            if (ThreadLocalRandom.current().nextInt(100) < 15) {

                return ThreadLocalRandom.current().nextInt(700, 1501);

            }

            return ThreadLocalRandom.current().nextInt(200, 501);

        default:
            return 200;
    }
}
}