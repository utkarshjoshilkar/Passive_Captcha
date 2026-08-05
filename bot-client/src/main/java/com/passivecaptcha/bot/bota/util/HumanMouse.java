package com.passivecaptcha.bot.bota.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class HumanMouse {

    private final Actions actions;

    public HumanMouse(WebDriver driver) {
        this.actions = new Actions(driver);
    }

    public void moveToElement(WebElement element) {

        int offsetX = random(-8, 8);
        int offsetY = random(-8, 8);

        actions.moveToElement(element, offsetX, offsetY)
                .pause(Duration.ofMillis(random(200, 700)))
                .perform();

        microAdjust();

    }

    private void microAdjust() {

        actions.moveByOffset(random(-2,2), random(-2,2))
                .pause(Duration.ofMillis(random(50,150)))
                .moveByOffset(random(-2,2), random(-2,2))
                .pause(Duration.ofMillis(random(50,150)))
                .perform();

    }

    private int random(int min,int max){
        return ThreadLocalRandom.current().nextInt(min,max+1);
    }
}