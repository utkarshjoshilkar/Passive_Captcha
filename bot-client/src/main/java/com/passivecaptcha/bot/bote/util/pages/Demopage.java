package com.passivecaptcha.bot.bote.util.pages;

public class Demopage {

    // Home Page
    public static final String VIEW_DEMO = "#view-demo-button";

    // Demo Page
    public static final String NAME = "#user-name";

    public static final String FEEDBACK = "#feedback";

    public static final String SUBMIT = "#submit-feedback";

    public static final String ANALYZE = "#analyze-button";

    public static String rating(int stars) {
        return "#rating-" + stars;
    }
}
