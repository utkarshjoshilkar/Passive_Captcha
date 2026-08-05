package com.passivecaptcha.bot.botd.util.pages;

public class DemoPage {

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