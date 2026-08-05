package com.passivecaptcha.bot.botd.util.behavior;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.passivecaptcha.bot.botd.util.brain.Memory;
import com.passivecaptcha.bot.botd.util.brain.Personality;

import java.util.concurrent.ThreadLocalRandom;

public class HumanScroll {

    private final Page page;
    private final Memory memory;

    public HumanScroll(Page page, Memory memory) {

        this.page = page;
        this.memory = memory;
    }

    public void scrollTo(String selector) throws InterruptedException {

        Locator element = page.locator(selector);

        if (element.isVisible()) {
            return;
        }

        Personality personality = memory.getPersonality();

        int scrollCount = 0;

        while (!element.isVisible()) {

            scrollCount++;

            int pixels = scrollDistance(personality)
                    + ThreadLocalRandom.current().nextInt(-30, 31);

            page.mouse().wheel(0, pixels);

            Thread.sleep(scrollPause(personality));

            // Reading pause every few scrolls
            if (scrollCount % ThreadLocalRandom.current().nextInt(2, 5) == 0) {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(350, 900)
                );
            }

            // Normal reading pause
            if (ThreadLocalRandom.current().nextInt(100) < 35) {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(150, 350)
                );
            }

            // Small reverse scroll
            if (ThreadLocalRandom.current().nextInt(100) < 12) {

                page.mouse().wheel(
                        0,
                        -ThreadLocalRandom.current()
                                .nextInt(20, 80)
                );

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(100, 250)
                );
            }
        }

        // Final alignment
        page.mouse().wheel(
                0,
                ThreadLocalRandom.current()
                        .nextInt(-120, 120)
        );

        Thread.sleep(
                ThreadLocalRandom.current()
                        .nextInt(120, 220)
        );

        // Cognitive updates
        memory.reduceAttention(1);
        memory.increaseConfidence(1);
    }

    private int scrollDistance(Personality personality) {

        return switch (personality) {

            case FAST ->
                    ThreadLocalRandom.current()
                            .nextInt(250, 420);

            case NORMAL ->
                    ThreadLocalRandom.current()
                            .nextInt(150, 300);

            case CAREFUL ->
                    ThreadLocalRandom.current()
                            .nextInt(80, 180);
        };
    }

    private int scrollPause(Personality personality) {

        return switch (personality) {

            case FAST ->
                    ThreadLocalRandom.current()
                            .nextInt(60, 120);

            case NORMAL ->
                    ThreadLocalRandom.current()
                            .nextInt(120, 220);

            case CAREFUL ->
                    ThreadLocalRandom.current()
                            .nextInt(220, 420);
        };
    }
}