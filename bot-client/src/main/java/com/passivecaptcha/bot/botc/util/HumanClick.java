package com.passivecaptcha.bot.botc.util;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class HumanClick {

    private final Actions actions;
    private final PersonalityProfile profile;

    public HumanClick(ChromeDriver driver,
                      PersonalityProfile profile) {

        this.actions = new Actions(driver);
        this.profile = profile;
    }

    /**
     * Move to the element and briefly focus it.
     */
    public void focus(WebElement element) {

        try {

            actions.moveToElement(element)
                    .pause(Duration.ofMillis(randomHoverPause()))
                    .perform();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * Human-like click with a slight hesitation.
     */
    public void click(WebElement element) {

        try {

            actions.moveToElement(element)
                    .pause(Duration.ofMillis(randomHoverPause()))
                    .click()
                    .perform();

            sleep(randomClickPause());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private int randomHoverPause() {

        return ThreadLocalRandom.current().nextInt(

                profile.getMinMouseDelay(),

                profile.getMaxMouseDelay() + 1
        );
    }

    private int randomClickPause() {

        return ThreadLocalRandom.current().nextInt(

                profile.getMinClickPause(),

                profile.getMaxClickPause() + 1
        );
    }

    private void sleep(int milliseconds) {

        try {

            Thread.sleep(milliseconds);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}