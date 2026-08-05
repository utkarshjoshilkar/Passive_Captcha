package com.passivecaptcha.bot.botc.util;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.concurrent.ThreadLocalRandom;

public class HumanTyping {

    private final PersonalityProfile profile;

    public HumanTyping(PersonalityProfile profile) {
        this.profile = profile;
    }

    public void type(WebElement element, String text) {

    element.clear();

    int burstLength = randomBurstLength();
    int typedInBurst = 0;

   for (char character : text.toCharArray()) {

    // QWERTY typo
    if (shouldMakeTypo(character)) {

        char wrong = randomWrongCharacter(character);

        // Type wrong character
        element.sendKeys(String.valueOf(wrong));

        sleep(randomTypingDelay());

        // Notice mistake
        sleep(randomThinkingPause() / 2);

        // Delete wrong character
        element.sendKeys(Keys.BACK_SPACE);

        sleep(randomTypingDelay());

        // Type correct character
        element.sendKeys(String.valueOf(character));

    } else {

        // Normal typing
        element.sendKeys(String.valueOf(character));
    }

    typedInBurst++;

    sleep(randomTypingDelay());

    // Pause after spaces
    if (character == ' ') {
        sleep(randomWordPause());
    }

    // Pause after punctuation
    if (isPunctuation(character)) {
        sleep(randomSentencePause());
    }

    // Burst typing
    if (typedInBurst >= burstLength) {

        sleep(randomReadingPause());

        typedInBurst = 0;
        burstLength = randomBurstLength();
    }

    // Small random hesitation
    if (ThreadLocalRandom.current().nextInt(100) < 5) {

        sleep(randomThinkingPause());
    }
}
}

    private boolean shouldMakeTypo(char c) {

        if (!Character.isLetter(c))
            return false;

        return ThreadLocalRandom.current().nextInt(100) < 7;
    }

    private char randomWrongCharacter(char actual) {

    char c = Character.toLowerCase(actual);

    String neighbours;

    switch (c) {

        case 'a': neighbours = "qwsz"; break;
        case 'b': neighbours = "vghn"; break;
        case 'c': neighbours = "xdfv"; break;
        case 'd': neighbours = "ersfcx"; break;
        case 'e': neighbours = "wsdr"; break;
        case 'f': neighbours = "rtgdcv"; break;
        case 'g': neighbours = "tyfhvb"; break;
        case 'h': neighbours = "yugjbn"; break;
        case 'i': neighbours = "ujko"; break;
        case 'j': neighbours = "uikhnm"; break;
        case 'k': neighbours = "ijolm"; break;
        case 'l': neighbours = "kop"; break;
        case 'm': neighbours = "njk"; break;
        case 'n': neighbours = "bhjm"; break;
        case 'o': neighbours = "iklp"; break;
        case 'p': neighbours = "ol"; break;
        case 'q': neighbours = "wa"; break;
        case 'r': neighbours = "edft"; break;
        case 's': neighbours = "awedxz"; break;
        case 't': neighbours = "rfgy"; break;
        case 'u': neighbours = "yhji"; break;
        case 'v': neighbours = "cfgb"; break;
        case 'w': neighbours = "qase"; break;
        case 'x': neighbours = "zsdc"; break;
        case 'y': neighbours = "tghu"; break;
        case 'z': neighbours = "asx"; break;

        default:

            neighbours = "abcdefghijklmnopqrstuvwxyz";
    }

    char wrong = neighbours.charAt(

            ThreadLocalRandom.current().nextInt(neighbours.length())

    );

    if (Character.isUpperCase(actual)) {

        wrong = Character.toUpperCase(wrong);

    }

    return wrong;
}

    private boolean isPunctuation(char c) {

        return c == '.'
                || c == ','
                || c == '!'
                || c == '?'
                || c == ':'
                || c == ';';
    }

    private int randomTypingDelay() {

        return ThreadLocalRandom.current().nextInt(
                profile.getMinTypingDelay(),
                profile.getMaxTypingDelay() + 1
        );
    }

    private int randomThinkingPause() {

        return ThreadLocalRandom.current().nextInt(
                profile.getMinThinkingDelay(),
                profile.getMaxThinkingDelay() + 1
        );
    }

    private int randomReadingPause() {

        return ThreadLocalRandom.current().nextInt(
                profile.getMinReadingPause(),
                profile.getMaxReadingPause() + 1
        );
    }

    private int randomWordPause() {
        return randomReadingPause() / 4;
    }

    private int randomSentencePause() {
        return randomReadingPause() / 2;
    }

    private int randomBurstLength() {
        return ThreadLocalRandom.current().nextInt(5, 11);
    }

    private void sleep(int milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}