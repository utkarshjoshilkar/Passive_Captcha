package com.passivecaptcha.bot.bote.util.behavior;

import java.util.concurrent.ThreadLocalRandom;

public enum SessionPersonality {

    FAST(
            0.8,
            0.6,
            0.4
    ),

    NORMAL(
            1.0,
            1.0,
            1.0
    ),

    CAREFUL(
            1.3,
            1.4,
            1.6
    );

    private final double movementFactor;
    private final double typingFactor;
    private final double pauseFactor;

    SessionPersonality(
            double movementFactor,
            double typingFactor,
            double pauseFactor
    ) {
        this.movementFactor = movementFactor;
        this.typingFactor = typingFactor;
        this.pauseFactor = pauseFactor;
    }

    public double movementFactor() {
        return movementFactor;
    }

    public double typingFactor() {
        return typingFactor;
    }

    public double pauseFactor() {
        return pauseFactor;
    }

    public static SessionPersonality random() {

        SessionPersonality[] values = values();

        return values[
                ThreadLocalRandom.current()
                        .nextInt(values.length)
        ];
    }
}