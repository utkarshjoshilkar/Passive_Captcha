package com.passivecaptcha.bot.botc.util;

public class PersonalityProfile {

    // Typing
    private final int minTypingDelay;
    private final int maxTypingDelay;

    // Thinking
    private final int minThinkingDelay;
    private final int maxThinkingDelay;

    // Scrolling
    private final int minScrollPause;
    private final int maxScrollPause;

    // Clicking
    private final int minClickPause;
    private final int maxClickPause;

    // Mouse movement
    private final int minMouseDelay;
    private final int maxMouseDelay;

    // Reading behaviour
    private final int minReadingPause;
    private final int maxReadingPause;

    // Idle behaviour
    private final int minIdlePause;
    private final int maxIdlePause;

    public PersonalityProfile(

            // Typing
            int minTypingDelay,
            int maxTypingDelay,

            // Thinking
            int minThinkingDelay,
            int maxThinkingDelay,

            // Scrolling
            int minScrollPause,
            int maxScrollPause,

            // Clicking
            int minClickPause,
            int maxClickPause,

            // Mouse
            int minMouseDelay,
            int maxMouseDelay,

            // Reading
            int minReadingPause,
            int maxReadingPause,

            // Idle
            int minIdlePause,
            int maxIdlePause
    ) {

        if (minTypingDelay > maxTypingDelay
                || minThinkingDelay > maxThinkingDelay
                || minScrollPause > maxScrollPause
                || minClickPause > maxClickPause
                || minMouseDelay > maxMouseDelay
                || minReadingPause > maxReadingPause
                || minIdlePause > maxIdlePause) {

            throw new IllegalArgumentException(
                    "Minimum delay cannot be greater than maximum delay."
            );
        }

        this.minTypingDelay = minTypingDelay;
        this.maxTypingDelay = maxTypingDelay;

        this.minThinkingDelay = minThinkingDelay;
        this.maxThinkingDelay = maxThinkingDelay;

        this.minScrollPause = minScrollPause;
        this.maxScrollPause = maxScrollPause;

        this.minClickPause = minClickPause;
        this.maxClickPause = maxClickPause;

        this.minMouseDelay = minMouseDelay;
        this.maxMouseDelay = maxMouseDelay;

        this.minReadingPause = minReadingPause;
        this.maxReadingPause = maxReadingPause;

        this.minIdlePause = minIdlePause;
        this.maxIdlePause = maxIdlePause;
    }

    // Typing
    public int getMinTypingDelay() {
        return minTypingDelay;
    }

    public int getMaxTypingDelay() {
        return maxTypingDelay;
    }

    // Thinking
    public int getMinThinkingDelay() {
        return minThinkingDelay;
    }

    public int getMaxThinkingDelay() {
        return maxThinkingDelay;
    }

    // Scroll
    public int getMinScrollPause() {
        return minScrollPause;
    }

    public int getMaxScrollPause() {
        return maxScrollPause;
    }

    // Click
    public int getMinClickPause() {
        return minClickPause;
    }

    public int getMaxClickPause() {
        return maxClickPause;
    }

    // Mouse
    public int getMinMouseDelay() {
        return minMouseDelay;
    }

    public int getMaxMouseDelay() {
        return maxMouseDelay;
    }

    // Reading
    public int getMinReadingPause() {
        return minReadingPause;
    }

    public int getMaxReadingPause() {
        return maxReadingPause;
    }

    // Idle
    public int getMinIdlePause() {
        return minIdlePause;
    }

    public int getMaxIdlePause() {
        return maxIdlePause;
    }
}