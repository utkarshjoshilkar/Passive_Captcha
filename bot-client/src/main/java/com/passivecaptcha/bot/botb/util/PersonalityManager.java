package com.passivecaptcha.bot.botb.util;

import java.util.concurrent.ThreadLocalRandom;

public class PersonalityManager {

    public static SessionPersonality randomPersonality() {

        SessionPersonality[] personalities = SessionPersonality.values();

        int index = ThreadLocalRandom.current()
                .nextInt(personalities.length);

        return personalities[index];

    }

}