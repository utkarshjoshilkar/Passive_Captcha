package com.passivecaptcha.bot.botc.util;

import java.util.concurrent.ThreadLocalRandom;

public class PersonalityManager {

    /**
     * Returns a random session personality.
     *
     * Distribution:
     * NORMAL  = 60%
     * FAST    = 20%
     * CAREFUL = 20%
     */
    public SessionPersonality getRandomPersonality() {

        int value = ThreadLocalRandom.current().nextInt(100);

        if (value < 20) {
            return SessionPersonality.FAST;
        }

        if (value < 80) {
            return SessionPersonality.NORMAL;
        }

        return SessionPersonality.CAREFUL;
    }

    /**
     * Returns the behavioural profile associated with a personality.
     */
    public PersonalityProfile getProfile(SessionPersonality personality) {

        switch (personality) {

            case FAST:

                return new PersonalityProfile(

                        // Typing
                        40, 80,

                        // Thinking
                        150, 400,

                        // Scroll
                        30, 70,

                        // Click
                        40, 90,

                        // Mouse
                        5, 20,

                        // Reading
                        300, 700,

                        // Idle
                        150, 400
                );

            case CAREFUL:

                return new PersonalityProfile(

                        // Typing
                        120, 180,

                        // Thinking
                        800, 1800,

                        // Scroll
                        120, 250,

                        // Click
                        180, 350,

                        // Mouse
                        35, 80,

                        // Reading
                        1800, 3500,

                        // Idle
                        800, 2000
                );

            default: // NORMAL

                return new PersonalityProfile(

                        // Typing
                        70, 120,

                        // Thinking
                        400, 900,

                        // Scroll
                        70, 120,

                        // Click
                        90, 180,

                        // Mouse
                        15, 40,

                        // Reading
                        800, 1800,

                        // Idle
                        400, 900
                );
        }
    }
}