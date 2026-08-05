package com.passivecaptcha.bot.botd.util.brain;

public class Memory {

    private final Personality personality;

    private int confidence;

    private int attention;

    public Memory(Personality personality) {

        this.personality = personality;

        switch (personality) {

            case FAST -> {
                confidence = 85;
                attention = 55;
            }

            case NORMAL -> {
                confidence = 70;
                attention = 70;
            }

            case CAREFUL -> {
                confidence = 55;
                attention = 90;
            }
        }
    }

    public Personality getPersonality() {
        return personality;
    }

    public int getConfidence() {
        return confidence;
    }

    public int getAttention() {
        return attention;
    }

    public void decreaseAttention(int value) {

        attention = Math.max(30, attention - value);
    }

    public void increaseConfidence(int value) {

        confidence = Math.min(100, confidence + value);
    }

    public void decreaseConfidence(int value) {

        confidence = Math.max(20, confidence - value);
    }
    public void reduceAttention(int amount) {

    attention = Math.max(20, attention - amount);
}

public void increaseAttention(int amount) {

    attention = Math.min(100, attention + amount);
}

public void reduceConfidence(int amount) {

    confidence = Math.max(20, confidence - amount);
}


}