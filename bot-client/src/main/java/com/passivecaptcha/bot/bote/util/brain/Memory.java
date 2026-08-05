package com.passivecaptcha.bot.bote.util.brain;

public class Memory {

    private int confidence;
    private int attention;
    private int patience;
    private int fatigue;

    public Memory() {

        confidence = 70;
        attention = 80;
        patience = 75;
        fatigue = 10;
    }

    public int getConfidence() {
        return confidence;
    }

    public int getAttention() {
        return attention;
    }

    public int getPatience() {
        return patience;
    }

    public int getFatigue() {
        return fatigue;
    }

    public void increaseConfidence(int value) {

        confidence = Math.min(100, confidence + value);
    }

    public void decreaseConfidence(int value) {

        confidence = Math.max(20, confidence - value);
    }

    public void increaseAttention(int value) {

        attention = Math.min(100, attention + value);
    }

    public void decreaseAttention(int value) {

        attention = Math.max(20, attention - value);
    }

    public void increasePatience(int value) {

        patience = Math.min(100, patience + value);
    }

    public void decreasePatience(int value) {

        patience = Math.max(10, patience - value);
    }

    public void increaseFatigue(int value) {

        fatigue = Math.min(100, fatigue + value);
    }

    public void decreaseFatigue(int value) {

        fatigue = Math.max(0, fatigue - value);
    }

    public void sessionTick() {

        fatigue = Math.min(100, fatigue + 1);

        attention = Math.max(20, attention - 1);

        patience = Math.max(10, patience - 1);
    }
}