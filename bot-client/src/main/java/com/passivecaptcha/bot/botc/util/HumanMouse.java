package com.passivecaptcha.bot.botc.util;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class HumanMouse {

    private final Actions actions;
    private final PersonalityProfile profile;

    public HumanMouse(ChromeDriver driver,
                      PersonalityProfile profile) {

        this.actions = new Actions(driver);
        this.profile = profile;
    }

    public void moveTo(WebElement element) {

        try {

            // Initial movement
            actions.moveToElement(element)
                    .pause(Duration.ofMillis(randomMousePause()))
                    .perform();

            // 40% chance of overshooting the target
            if (ThreadLocalRandom.current().nextInt(100) < 40) {

                int overshootX = ThreadLocalRandom.current().nextInt(-8, 9);
                int overshootY = ThreadLocalRandom.current().nextInt(-8, 9);

                actions.moveByOffset(overshootX, overshootY)
                        .pause(Duration.ofMillis(randomMousePause()))
                        .perform();

                // Return to target
                actions.moveToElement(element)
                        .pause(Duration.ofMillis(randomMousePause()))
                        .perform();
            }

            // Micro corrections
            int corrections = randomCorrections();

            for (int i = 0; i < corrections; i++) {

                int range = randomOffsetRange();

                int x = ThreadLocalRandom.current().nextInt(-range, range + 1);
                int y = ThreadLocalRandom.current().nextInt(-range, range + 1);

                actions.moveByOffset(x, y)
                        .pause(Duration.ofMillis(randomMousePause()))
                        .perform();
            }

            // Final alignment
            actions.moveToElement(element)
                    .pause(Duration.ofMillis(randomMousePause()))
                    .perform();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private int randomCorrections() {

        int delay = profile.getMaxMouseDelay();

        if (delay <= 20)
            return ThreadLocalRandom.current().nextInt(1,3);

        if (delay <= 40)
            return ThreadLocalRandom.current().nextInt(2,5);

        return ThreadLocalRandom.current().nextInt(4,7);
    }

    private int randomOffsetRange() {

        int delay = profile.getMaxMouseDelay();

        if (delay <= 20)
            return 2;

        if (delay <= 40)
            return 4;

        return 6;
    }

    private int randomMousePause() {

        return ThreadLocalRandom.current().nextInt(
                profile.getMinMouseDelay(),
                profile.getMaxMouseDelay() + 1
        );
    }
}