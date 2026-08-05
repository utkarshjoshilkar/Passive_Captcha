package com.passivecaptcha.bot.bota.util;

import org.openqa.selenium.WebElement;

import java.util.concurrent.ThreadLocalRandom;

public class HumanBehavior {

    public static void sleep(int minMs, int maxMs) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(minMs, maxMs + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void typeLikeHuman(WebElement element, String text) {

        element.click();

        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            typingDelay();
        }
    }

    // Reading page
    public static void readingPause() {

        if (PersonalityManager.isFast()) {
            sleep(1000, 2500);
        } else if (PersonalityManager.isCareful()) {
            sleep(3500, 7000);
        } else if (PersonalityManager.isDistracted()) {
            sleep(2500, 6500);
        } else { // DECISIVE
            sleep(1500, 3000);
        }
    }

    // Thinking before an action
    public static void thinkingPause() {

        if (PersonalityManager.isFast()) {
            sleep(300, 800);
        } else if (PersonalityManager.isCareful()) {
            sleep(1500, 3500);
        } else if (PersonalityManager.isDistracted()) {
            sleep(800, 4000);
        } else {
            sleep(300, 1000);
        }
    }

    // Pause after completing an action
    public static void actionPause() {

        if (PersonalityManager.isFast()) {
            sleep(400, 1000);
        } else if (PersonalityManager.isCareful()) {
            sleep(1200, 3000);
        } else if (PersonalityManager.isDistracted()) {
            sleep(800, 3500);
        } else {
            sleep(600, 1500);
        }
    }

    // Hover before clicking
    public static void hoverPause() {

        if (PersonalityManager.isFast()) {
            sleep(100, 300);
        } else if (PersonalityManager.isCareful()) {
            sleep(700, 1500);
        } else if (PersonalityManager.isDistracted()) {
            sleep(300, 1200);
        } else {
            sleep(150, 500);
        }
    }

    // Idle cursor
    public static void idlePause() {

        if (PersonalityManager.isFast()) {
            sleep(500, 1200);
        } else if (PersonalityManager.isCareful()) {
            sleep(2500, 5000);
        } else if (PersonalityManager.isDistracted()) {
            sleep(3000, 7000);
        } else {
            sleep(800, 1800);
        }
    }

    // Typing delay
    public static void typingDelay() {

        if (PersonalityManager.isFast()) {
            sleep(40, 90);
        } else if (PersonalityManager.isCareful()) {
            sleep(140, 250);
        } else if (PersonalityManager.isDistracted()) {
            sleep(80, 220);
        } else {
            sleep(60, 130);
        }
    }
}