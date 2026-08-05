package com.passivecaptcha.bot.bote.util.behavior;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.concurrent.ThreadLocalRandom;

public class HumanClickV2 {

    private final Page page;
    private final HumanMouseV2 mouse;
    private final SessionPersonality personality;

    public HumanClickV2(Page page,
                        HumanMouseV2 mouse,
                        SessionPersonality personality) {

        this.page = page;
        this.mouse = mouse;
        this.personality = personality;
    }

    public void click(String selector)
            throws InterruptedException {

        mouse.moveTo(selector);

        Thread.sleep(preClickDelay());

        Locator element = page.locator(selector);

        if (!element.isVisible())
            return;

        if (!element.isEnabled())
            return;

        element.click();

        Thread.sleep(postClickDelay());
    }

    private int preClickDelay() {

        return switch (personality) {

            case FAST ->
                    ThreadLocalRandom.current()
                            .nextInt(40,90);

            case NORMAL ->
                    ThreadLocalRandom.current()
                            .nextInt(80,180);

            case CAREFUL ->
                    ThreadLocalRandom.current()
                            .nextInt(180,320);
        };
    }

    private int postClickDelay() {

        return switch (personality) {

            case FAST ->
                    ThreadLocalRandom.current()
                            .nextInt(40,100);

            case NORMAL ->
                    ThreadLocalRandom.current()
                            .nextInt(80,180);

            case CAREFUL ->
                    ThreadLocalRandom.current()
                            .nextInt(180,350);
        };
    }
}