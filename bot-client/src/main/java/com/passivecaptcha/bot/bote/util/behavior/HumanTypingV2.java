package com.passivecaptcha.bot.bote.util.behavior;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.concurrent.ThreadLocalRandom;

public class HumanTypingV2 {

    private final Page page;
    private final SessionPersonality personality;

    public HumanTypingV2(Page page,
                         SessionPersonality personality) {

        this.page = page;
        this.personality = personality;
    }

    public void type(String selector,
                     String text)
            throws InterruptedException {

        Locator field = page.locator(selector);

        field.click();

        Thread.sleep(initialPause());

        for (char c : text.toCharArray()) {

            field.pressSequentially(String.valueOf(c));

            Thread.sleep(keyDelay());

            if (c == ' ') {

                Thread.sleep(wordPause());
            }

            if (isSentenceEnd(c)) {

                Thread.sleep(sentencePause());
            }
        }
    }

    private int keyDelay() {

        return switch (personality) {

            case FAST ->
                    ThreadLocalRandom.current()
                            .nextInt(40,70);

            case NORMAL ->
                    ThreadLocalRandom.current()
                            .nextInt(70,120);

            case CAREFUL ->
                    ThreadLocalRandom.current()
                            .nextInt(120,180);
        };
    }

    private int initialPause() {

        return switch (personality) {

            case FAST ->
                    ThreadLocalRandom.current()
                            .nextInt(150,300);

            case NORMAL ->
                    ThreadLocalRandom.current()
                            .nextInt(300,500);

            case CAREFUL ->
                    ThreadLocalRandom.current()
                            .nextInt(500,800);
        };
    }

    private int wordPause() {

        return ThreadLocalRandom.current()
                .nextInt(80,180);
    }

    private int sentencePause() {

        return ThreadLocalRandom.current()
                .nextInt(250,500);
    }

    private boolean isSentenceEnd(char c) {

        return c == '.'
                || c == '!'
                || c == '?';
    }
}