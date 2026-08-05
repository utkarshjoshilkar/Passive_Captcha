package com.passivecaptcha.bot.botc.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.ThreadLocalRandom;

public class HumanScroll {

    private final ChromeDriver driver;
    private final PersonalityProfile profile;

    public HumanScroll(ChromeDriver driver,
                       PersonalityProfile profile) {

        this.driver = driver;
        this.profile = profile;
    }

public void scrollTo(WebElement element) {

    try {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Don't scroll if element is already visible
        Boolean visible = (Boolean) js.executeScript(
    """
    const rect = arguments[0].getBoundingClientRect();
    return (
        rect.top >= 100 &&
        rect.bottom <= window.innerHeight - 100
    );
    """,
    element
);

if (Boolean.TRUE.equals(visible)) {
    return;
}

        long targetY = ((Number) js.executeScript(
                "return arguments[0].getBoundingClientRect().top + window.pageYOffset;",
                element
        )).longValue();

        long currentY = ((Number) js.executeScript(
                "return window.pageYOffset;"
        )).longValue();

        boolean overscroll =
                ThreadLocalRandom.current().nextInt(100) < 30;

        boolean midPause =
                ThreadLocalRandom.current().nextInt(100) < 40;

        while (Math.abs(targetY - currentY) > 70) {

            int step = randomScrollStep();

            if (targetY > currentY) {
                currentY += step;
            } else {
                currentY -= step;
            }

            js.executeScript(
                    "window.scrollTo(0, arguments[0]);",
                    currentY
            );

            sleep(randomScrollPause());

            if (midPause &&
                    Math.abs(targetY - currentY) < 300 &&
                    Math.abs(targetY - currentY) > 150) {

                sleep(randomReadingPause());

                midPause = false;
            }
        }

        if (overscroll) {

            int extra =
                    ThreadLocalRandom.current().nextInt(80, 180);

            js.executeScript(
                    "window.scrollTo(0, arguments[0]);",
                    targetY + extra
            );

            sleep(randomScrollPause());

        }

        // Final human-like positioning (not exactly at top)
        js.executeScript(
                "window.scrollTo(0, arguments[0] - 150);",
                targetY
        );

        sleep(randomScrollPause());

        // Tiny adjustment
        if (ThreadLocalRandom.current().nextInt(100) < 40) {

            int adjust =
                    ThreadLocalRandom.current().nextInt(-20, 21);

            js.executeScript(
                    "window.scrollBy(0, arguments[0]);",
                    adjust
            );

            sleep(randomScrollPause());
        }

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    private int randomScrollStep() {

        int delay = profile.getMaxMouseDelay();

        if (delay <= 20)
            return ThreadLocalRandom.current().nextInt(90,180);

        if (delay <= 40)
            return ThreadLocalRandom.current().nextInt(60,130);

        return ThreadLocalRandom.current().nextInt(30,90);
    }

    private int randomScrollPause() {

        return ThreadLocalRandom.current().nextInt(

                profile.getMinScrollPause(),

                profile.getMaxScrollPause() + 1

        );
    }

    private int randomReadingPause() {

        return ThreadLocalRandom.current().nextInt(

                profile.getMinReadingPause(),

                profile.getMaxReadingPause() + 1

        );
    }

    private void sleep(int ms) {

        try {

            Thread.sleep(ms);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}