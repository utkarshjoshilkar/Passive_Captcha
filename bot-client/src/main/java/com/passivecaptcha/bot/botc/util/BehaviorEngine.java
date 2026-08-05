package com.passivecaptcha.bot.botc.util;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.ThreadLocalRandom;

public class BehaviorEngine {

    private final HumanScroll humanScroll;
    private final HumanTyping humanTyping;
    private final HumanClick humanClick;
    private final HumanMouse humanMouse;

    private final PersonalityManager personalityManager;
    private final SessionPersonality personality;
    private final PersonalityProfile profile;

    public BehaviorEngine(ChromeDriver driver) {

        personalityManager = new PersonalityManager();

        personality = personalityManager.getRandomPersonality();

        profile = personalityManager.getProfile(personality);

        humanScroll = new HumanScroll(driver, profile);
        humanTyping = new HumanTyping(profile);
        humanClick = new HumanClick(driver, profile);
        humanMouse = new HumanMouse(driver, profile);

        System.out.println("--------------------------------");
        System.out.println("Session Personality : " + personality);
        System.out.println("--------------------------------");
    }

    /*
     * ==========================
     * High Level Behaviour
     * ==========================
     */

    public void type(WebElement element, String text) {

        think();

        humanScroll.scrollTo(element);

        humanMouse.moveTo(element);

        humanClick.focus(element);

        humanTyping.type(element, text);
    }

    public void click(WebElement element) {

        think();

        humanScroll.scrollTo(element);

        humanMouse.moveTo(element);

        humanClick.click(element);
    }

    public void move(WebElement element) {

        think();

        humanScroll.scrollTo(element);

        humanMouse.moveTo(element);
    }

    public void scroll(WebElement element) {

        think();

        humanScroll.scrollTo(element);
    }

    /*
     * ==========================
     * Behaviour Pauses
     * ==========================
     */

    public void think() {

        pause(
                profile.getMinThinkingDelay(),
                profile.getMaxThinkingDelay()
        );
    }

    public void read() {

        pause(
                profile.getMinReadingPause(),
                profile.getMaxReadingPause()
        );
    }

    public void idle() {

        pause(
                profile.getMinIdlePause(),
                profile.getMaxIdlePause()
        );
    }

    /*
     * ==========================
     * Common Pause Utility
     * ==========================
     */

    private void pause(int min, int max) {

        try {

            Thread.sleep(

                    ThreadLocalRandom.current().nextInt(

                            min,

                            max + 1
                    )

            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }

    /*
     * ==========================
     * Getters
     * ==========================
     */

    public SessionPersonality getPersonality() {
        return personality;
    }

    public PersonalityProfile getProfile() {
        return profile;
    }
}