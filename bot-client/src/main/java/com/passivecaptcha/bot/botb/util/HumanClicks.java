package com.passivecaptcha.bot.botb.util;
import java.util.concurrent.ThreadLocalRandom;

import com.microsoft.playwright.Page;

public class HumanClicks {

    private final Page page;
    private final HumanMouse mouse;
    private final SessionPersonality personality;

    public HumanClicks(Page page,HumanMouse mouse,SessionPersonality personality) {

    this.page = page;
    this.mouse = mouse;
    this.personality = personality;
}

    public void click(String selector) throws InterruptedException {

        mouse.moveToElement(selector);

        page.locator(selector).click();
        Thread.sleep(getClickPause());
    }

    public void doubleClick(String selector) throws InterruptedException {

        mouse.moveToElement(selector);

        page.locator(selector).dblclick();
        Thread.sleep(getClickPause());

    }

    public void rightClick(String selector) throws InterruptedException {

        mouse.moveToElement(selector);

        page.locator(selector).click(
                new com.microsoft.playwright.Locator.ClickOptions()
                        .setButton(com.microsoft.playwright.options.MouseButton.RIGHT)
        );
        Thread.sleep(getClickPause());

    }
    private int getClickPause() {

    switch (personality) {

        case FAST:
            return ThreadLocalRandom.current().nextInt(80, 201);

        case NORMAL:
            return ThreadLocalRandom.current().nextInt(200, 451);

        case CAREFUL:
            return ThreadLocalRandom.current().nextInt(500, 901);

        case DISTRACTED:

            if (ThreadLocalRandom.current().nextInt(100) < 15) {
                return ThreadLocalRandom.current().nextInt(1200, 2501);
            }

            return ThreadLocalRandom.current().nextInt(300, 701);

        default:
            return 300;
    }
}
}