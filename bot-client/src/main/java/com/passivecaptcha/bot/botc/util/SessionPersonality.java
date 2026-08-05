package com.passivecaptcha.bot.botc.util;

public enum SessionPersonality {

    FAST("Fast"),
    NORMAL("Normal"),
    CAREFUL("Careful");

    private final String displayName;

    SessionPersonality(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}