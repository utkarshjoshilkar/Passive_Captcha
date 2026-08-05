package com.passivecaptcha.bot.bote.util.behavior;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.concurrent.ThreadLocalRandom;

public class HumanScrollV2 {

    private final Page page;
    private final SessionPersonality personality;

    public HumanScrollV2(Page page,
                         SessionPersonality personality) {

        this.page = page;
        this.personality = personality;
    }

    public void scrollTo(String selector)
            throws InterruptedException {

        Locator element = page.locator(selector);

        while (!element.isVisible()) {

            page.mouse().wheel(0, scrollDistance());

            Thread.sleep(scrollPause());
        }

        Thread.sleep(
                ThreadLocalRandom.current()
                        .nextInt(120,250)
        );
    }

    public void smallScrollDown()
            throws InterruptedException {

        page.mouse().wheel(
                0,
                ThreadLocalRandom.current()
                        .nextInt(80,180)
        );

        Thread.sleep(scrollPause());
    }

    public void smallScrollUp()
            throws InterruptedException {

        page.mouse().wheel(
                0,
                -ThreadLocalRandom.current()
                        .nextInt(80,180)
        );

        Thread.sleep(scrollPause());
    }

    private int scrollDistance() {

        return switch (personality) {

            case FAST ->
                    ThreadLocalRandom.current()
                            .nextInt(220,380);

            case NORMAL ->
                    ThreadLocalRandom.current()
                            .nextInt(140,260);

            case CAREFUL ->
                    ThreadLocalRandom.current()
                            .nextInt(80,160);
        };
    }

    private int scrollPause() {

        return switch (personality) {

            case FAST ->
                    ThreadLocalRandom.current()
                            .nextInt(60,120);

            case NORMAL ->
                    ThreadLocalRandom.current()
                            .nextInt(120,220);

            case CAREFUL ->
                    ThreadLocalRandom.current()
                            .nextInt(220,380);
        };
    }
}