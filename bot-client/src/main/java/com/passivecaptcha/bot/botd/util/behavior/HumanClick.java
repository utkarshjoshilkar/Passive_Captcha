package com.passivecaptcha.bot.botd.util.behavior;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.passivecaptcha.bot.botd.util.brain.Memory;
import com.passivecaptcha.bot.botd.util.brain.Personality;

import java.util.concurrent.ThreadLocalRandom;

public class HumanClick {

    private final Page page;
    private final Memory memory;
    private final HumanMouse mouse;

    public HumanClick(Page page,
                      Memory memory,
                      HumanMouse mouse) {

        this.page = page;
        this.memory = memory;
        this.mouse = mouse;
    }

    public void click(String selector) throws InterruptedException {

        mouse.moveTo(selector);
        Locator element = page.locator(selector);
        preClickPause();

        if (!element.isVisible()) {

    return;
}

if (!element.isEnabled()) {

    return;
}
if (ThreadLocalRandom.current().nextInt(100) < 5) {

    Thread.sleep(

            ThreadLocalRandom.current()

                    .nextInt(300,700)
    );
}

        element.click();
        if (ThreadLocalRandom.current().nextInt(100) < 2) {

    Thread.sleep(
            ThreadLocalRandom.current().nextInt(80, 150)
    );

    element.click();
}
        if (ThreadLocalRandom.current().nextInt(100) < 12) {

    Thread.sleep(

            ThreadLocalRandom.current()

                    .nextInt(120,250)
    );
}

       postClickPause();

if (ThreadLocalRandom.current().nextInt(100) < 15) {

    Thread.sleep(
            ThreadLocalRandom.current().nextInt(150, 300)
    );
}
       memory.reduceAttention(1);
       memory.increaseConfidence(1);
    }

   private void preClickPause() throws InterruptedException {

    Personality personality = memory.getPersonality();

    int min;
    int max;

    switch (personality) {

        case FAST -> {
            min = 30;
            max = 80;
        }

        case NORMAL -> {
            min = 80;
            max = 180;
        }

        default -> {
            min = 180;
            max = 350;
        }
    }

    Thread.sleep(
            ThreadLocalRandom.current().nextInt(min, max)
    );
}

    private void postClickPause() throws InterruptedException {

        if (ThreadLocalRandom.current().nextInt(100) < 25) {

            Thread.sleep(
                    ThreadLocalRandom.current().nextInt(100, 250)
            );
        }
    }
}