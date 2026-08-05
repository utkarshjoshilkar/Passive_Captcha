package com.passivecaptcha.bot.botb.util;

import com.microsoft.playwright.Page;

import java.util.concurrent.ThreadLocalRandom;

public class HumanScroll {

    private final Page page;
private final SessionPersonality personality;

    public HumanScroll(Page page, SessionPersonality personality) {
        this.page = page;
        this.personality = personality;
    }

    public void scrollDownSmall() throws InterruptedException {

        int pixels = ThreadLocalRandom.current().nextInt(150, 401);

        page.mouse().wheel(0, pixels);

        Thread.sleep(getShortScrollPause());
    }

    public void scrollUpSmall() throws InterruptedException {

        int pixels = ThreadLocalRandom.current().nextInt(150, 401);

        page.mouse().wheel(0, -pixels);

        Thread.sleep(getShortScrollPause());
    }

    public void scrollDownMedium() throws InterruptedException {

        int pixels = ThreadLocalRandom.current().nextInt(500, 901);

        page.mouse().wheel(0, pixels);

        Thread.sleep(getMediumScrollPause());
    }

    public void scrollToTop() throws InterruptedException {

        page.evaluate("window.scrollTo(0,0)");

        Thread.sleep(getShortScrollPause());
    }

    public void scrollToBottom() throws InterruptedException {

        page.evaluate("window.scrollTo(0,document.body.scrollHeight)");

        Thread.sleep(getShortScrollPause());
    }
    private int getShortScrollPause() {

    switch (personality) {

        case FAST:
            return ThreadLocalRandom.current().nextInt(100, 251);

        case NORMAL:
            return ThreadLocalRandom.current().nextInt(250, 501);

        case CAREFUL:
            return ThreadLocalRandom.current().nextInt(700, 1301);

        case DISTRACTED:

            if (ThreadLocalRandom.current().nextInt(100) < 15) {

                return ThreadLocalRandom.current().nextInt(1200, 2501);

            }

            return ThreadLocalRandom.current().nextInt(300, 701);

        default:
            return 300;
    }
}
private int getMediumScrollPause() {

    switch (personality) {

        case FAST:
            return ThreadLocalRandom.current().nextInt(300, 601);

        case NORMAL:
            return ThreadLocalRandom.current().nextInt(700, 1201);

        case CAREFUL:
            return ThreadLocalRandom.current().nextInt(1500, 2501);

        case DISTRACTED:

            if (ThreadLocalRandom.current().nextInt(100) < 20) {

                return ThreadLocalRandom.current().nextInt(2500, 4001);

            }

            return ThreadLocalRandom.current().nextInt(900, 1701);

        default:
            return 800;
    }
}

}