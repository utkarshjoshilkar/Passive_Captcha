package com.passivecaptcha.bot.bota.util;

import java.util.concurrent.ThreadLocalRandom;

public class PersonalityManager {

    private static SessionPersonality currentPersonality;

    public static void initialize() {

        SessionPersonality[] personalities = SessionPersonality.values();

        currentPersonality =
                personalities[
                        ThreadLocalRandom.current()
                                .nextInt(personalities.length)
                ];

        System.out.println("----------------------------------");
        System.out.println("Session Personality : " + currentPersonality);
        System.out.println("----------------------------------");
    }

    public static SessionPersonality getCurrentPersonality() {
        return currentPersonality;
    }

    public static boolean isFast() {
        return currentPersonality == SessionPersonality.FAST;
    }

    public static boolean isCareful() {
        return currentPersonality == SessionPersonality.CAREFUL;
    }

    public static boolean isDistracted() {
        return currentPersonality == SessionPersonality.DISTRACTED;
    }

    public static boolean isDecisive() {
        return currentPersonality == SessionPersonality.DECISIVE;
    }
}