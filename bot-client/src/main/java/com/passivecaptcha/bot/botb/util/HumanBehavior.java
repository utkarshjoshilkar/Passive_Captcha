package com.passivecaptcha.bot.botb.util;

import java.util.concurrent.ThreadLocalRandom;

public class HumanBehavior {

    public static void shortPause(SessionPersonality personality) throws InterruptedException {

    switch (personality) {

        case FAST:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(150,351));

            break;

        case NORMAL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(300,701));

            break;

        case CAREFUL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(600,1201));

            break;

        case DISTRACTED:

            if (chance(15)) {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(1200,2501));

            } else {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(400,801));

            }

            break;
    }

}

    public static void mediumPause(SessionPersonality personality) throws InterruptedException {
        switch (personality) {

        case FAST:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(150,351));

            break;

        case NORMAL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(300,701));

            break;

        case CAREFUL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(600,1201));

            break;

        case DISTRACTED:

            if (chance(15)) {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(1200,2501));

            } else {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(400,801));

            }

            break;
    }
    }

    public static void longPause(SessionPersonality personality) throws InterruptedException {
        switch (personality) {

        case FAST:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(150,351));

            break;

        case NORMAL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(300,701));

            break;

        case CAREFUL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(600,1201));

            break;

        case DISTRACTED:

            if (chance(15)) {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(1200,2501));

            } else {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(400,801));

            }

            break;
    }
    }

    public static void readingPause(SessionPersonality personality) throws InterruptedException {
        switch (personality) {

        case FAST:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(150,351));

            break;

        case NORMAL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(300,701));

            break;

        case CAREFUL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(600,1201));

            break;

        case DISTRACTED:

            if (chance(15)) {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(1200,2501));

            } else {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(400,801));

            }

            break;
    }
    }

    public static void thinkingPause(SessionPersonality personality) throws InterruptedException {
        switch (personality) {

        case FAST:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(150,351));

            break;

        case NORMAL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(300,701));

            break;

        case CAREFUL:

            Thread.sleep(
                    ThreadLocalRandom.current()
                            .nextInt(600,1201));

            break;

        case DISTRACTED:

            if (chance(15)) {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(1200,2501));

            } else {

                Thread.sleep(
                        ThreadLocalRandom.current()
                                .nextInt(400,801));

            }

            break;
    }
    }
    public static boolean chance(int percentage) {
        return ThreadLocalRandom.current().nextInt(100) < percentage;
    }
}