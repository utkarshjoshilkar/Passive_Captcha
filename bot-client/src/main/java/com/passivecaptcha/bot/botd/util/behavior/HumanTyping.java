package com.passivecaptcha.bot.botd.util.behavior;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.passivecaptcha.bot.botd.util.brain.Memory;
import com.passivecaptcha.bot.botd.util.brain.Personality;

import java.util.concurrent.ThreadLocalRandom;

public class HumanTyping {

    private final Page page;
    private final Memory memory;

    public HumanTyping(Page page, Memory memory) {

        this.page = page;
        this.memory = memory;
    }

    public void type(String selector, String text)
            throws InterruptedException {

        Locator field = page.locator(selector);

        field.click();

        thinkingPause();

        int burstLength = randomBurst();
        int typed = 0;

        for (char c : text.toCharArray()) {

            if (shouldMakeTypo(c)) {

    char wrong = wrongCharacter(c);

    field.pressSequentially(String.valueOf(wrong));

    Thread.sleep(keyDelay());

    field.press("Backspace");

    Thread.sleep(keyDelay());

    memory.reduceConfidence(2);
}

field.pressSequentially(String.valueOf(c));

memory.reduceAttention(1);

            typed++;

            Thread.sleep(keyDelay());

            if (c == ' ') {

                Thread.sleep(wordPause());
            }

            if (isSentenceEnd(c)) {

                Thread.sleep(sentencePause());
            }

            if (typed >= burstLength) {

                Thread.sleep(readingPause());

                typed = 0;

                burstLength = randomBurst();
            }
        }
    }

    private int keyDelay() {

    int attention = memory.getAttention();
    int confidence = memory.getConfidence();

    Personality personality = memory.getPersonality();

    int base = switch (personality) {

        case FAST -> 90;

        case NORMAL -> 60;

        case CAREFUL -> 50;
    };

    base += (100 - attention) / 3;
    base += (70 - confidence) / 4;

    return ThreadLocalRandom.current()
            .nextInt(base - 15, base + 20);
}
private boolean shouldMakeTypo(char c) {

    if (!Character.isLetter(c))
        return false;

    return ThreadLocalRandom.current().nextInt(100) < 5;
}
    private void thinkingPause()
            throws InterruptedException {

        Thread.sleep(

                ThreadLocalRandom.current().nextInt(

                        300,

                        700
                )
        );
    }

    private int wordPause() {

        return ThreadLocalRandom.current().nextInt(80,180);
    }

    private int sentencePause() {

        return ThreadLocalRandom.current().nextInt(300,700);
    }

    private int readingPause() {

        return ThreadLocalRandom.current().nextInt(250,600);
    }

    private int randomBurst() {

        return ThreadLocalRandom.current().nextInt(4,9);
    }

    private boolean isSentenceEnd(char c) {

        return c=='.'
                || c=='!'
                || c=='?';
    }
    private char wrongCharacter(char c) {

    c = Character.toLowerCase(c);

    return switch (c) {

        case 'a' -> 's';
        case 'b' -> 'v';
        case 'c' -> 'x';
        case 'd' -> 'f';
        case 'e' -> 'r';
        case 'f' -> 'g';
        case 'g' -> 'h';
        case 'h' -> 'j';
        case 'i' -> 'o';
        case 'j' -> 'k';
        case 'k' -> 'l';
        case 'l' -> 'k';
        case 'm' -> 'n';
        case 'n' -> 'm';
        case 'o' -> 'i';
        case 'p' -> 'o';
        case 'q' -> 'w';
        case 'r' -> 't';
        case 's' -> 'a';
        case 't' -> 'y';
        case 'u' -> 'y';
        case 'v' -> 'c';
        case 'w' -> 'q';
        case 'x' -> 'z';
        case 'y' -> 'u';
        case 'z' -> 'x';

        default -> c;
    };
}

}