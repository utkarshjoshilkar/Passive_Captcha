package com.passivecaptcha.bot;
import com.microsoft.playwright.*;

import com.passivecaptcha.bot.botb.util.HumanBehavior;
import com.passivecaptcha.bot.botb.util.HumanMouse;
import com.passivecaptcha.bot.botb.util.HumanScroll;
import com.passivecaptcha.bot.botb.util.HumanClicks;
import com.passivecaptcha.bot.botb.util.HumanTyping;

import com.passivecaptcha.bot.botb.util.PersonalityManager;
import com.passivecaptcha.bot.botb.util.SessionPersonality;

import com.passivecaptcha.bot.botb.util.BrowserProfile;
import com.passivecaptcha.bot.botb.util.VariantEnvironment;

import java.util.concurrent.ThreadLocalRandom;

public class BotB {

    private static final String BASE_URL = "http://localhost:3000";
    private static final String[] NAMES = {
    "Aarav jhah ",
    "Vihaan mohitepatile",
    "Aditya kashyap",
    "Rohan mithun pande",
    "Karan kundra",
    "Neha khapra",
    "Priya sameer rastogi",
    "Sneha mohit palsinghsode",
    "Ananya sneha kal singh",
    "Rahul vyadyasingh",
    "Utkarsh joshilkar",
    "Vaidehi arun pande",
    "Ishita",
    "Siddharth",
    "Aditi"
    };
    private static final String[] FEEDBACKS = {
        "The application is easy to use.I like it",
        "Navigation is smooth and intuitive.",
        "The demo works well.i like its interface, i love using this",
        "Overall a pleasant experience.it feels safe to use and interaction",
        "Everything responded quickly. everything is good , it works well",
        "The interface looks clean. and it is userfriendly even if some one dont know",
        "The form was simple to complete. it is so easy to use well done to the team who developed this",
        "Good design and responsiveness, i like it",
        "Nice interaction with the application.",
        "The demo was informative, i like it"
    };
    private static final int[] RATINGS = {
        3,4,5,4,5,3,4,5
    };

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    private HumanMouse mouse;
    private HumanScroll scroll;
    private HumanClicks clicks;
    private HumanTyping typing;

    private SessionPersonality personality;
    private BrowserProfile browserProfile;

    private String sessionName;
    private String sessionFeedback;
    private int sessionRating;
    
    public static void main(String[] args) {

    for (int session = 1; session <= 30; session++) {

        System.out.println("\n====================================");
        System.out.println("SESSION " + session + " OF 60");
        System.out.println("====================================");

        BotB bot = new BotB();
        bot.initializeSessionData();

        System.out.println("Name     : " + bot.sessionName);
        System.out.println("Rating   : " + bot.sessionRating);
        System.out.println("Feedback : " + bot.sessionFeedback);


        try {

            bot.personality = PersonalityManager.randomPersonality();
            bot.browserProfile = VariantEnvironment.randomProfile();

            System.out.println("--------------------------------");
            System.out.println("BotB V5");
            System.out.println("--------------------------------");
            System.out.println("Personality : " + bot.personality);
            System.out.println("--------------------------------");
            System.out.println("Viewport    : "
                    + bot.browserProfile.getWidth()
                    + " x "
                    + bot.browserProfile.getHeight());

            System.out.println("Locale      : "
                    + bot.browserProfile.getLocale());

            System.out.println("Timezone    : "
                    + bot.browserProfile.getTimezone());

            System.out.println("Theme       : "
                    + bot.browserProfile.getColorScheme());

            System.out.println("--------------------------------");
            bot.launchBrowser();
            HumanBehavior.mediumPause(bot.personality);

            bot.openApplication();
            HumanBehavior.readingPause(bot.personality);

            bot.scroll.scrollDownSmall();

            bot.clickViewDemo();
            HumanBehavior.mediumPause(bot.personality);

            bot.waitForDemoPage();
            HumanBehavior.readingPause(bot.personality);
            

            bot.fillName();
            HumanBehavior.shortPause(bot.personality);

            bot.selectRating();
            bot.scroll.scrollUpSmall();

            bot.fillFeedback();
            HumanBehavior.thinkingPause(bot.personality);

            bot.submitFeedback();
            bot.scroll.scrollToTop();

            bot.clickAnalyze();

            // Wait for backend processing and DB storage
           HumanBehavior.longPause(bot.personality);

            System.out.println("Session " + session + " completed successfully.");

        } catch (Exception e) {

            System.out.println("Session " + session + " failed.");
            e.printStackTrace();

        } finally {

            bot.closeBrowser();

            // Small gap before starting the next session
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
        }
    }

    System.out.println("\n====================================");
    System.out.println("ALL 60 SESSIONS COMPLETED");
    System.out.println("====================================");
}

    public void launchBrowser() {

    System.out.println("\n====================================");
    System.out.println("Launching Browser...");
    System.out.println("====================================");

    playwright = Playwright.create();

    browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                    .setHeadless(false)
    );

    Browser.NewContextOptions options =
        new Browser.NewContextOptions()

                .setViewportSize(
                        browserProfile.getWidth(),
                        browserProfile.getHeight())

                .setLocale(
                        browserProfile.getLocale())

                .setTimezoneId(
                        browserProfile.getTimezone())

                .setColorScheme(
                        browserProfile.getColorScheme());

    context = browser.newContext(options);
    page = context.newPage();

    mouse = new HumanMouse(page, personality);
    scroll = new HumanScroll(page, personality);
    clicks = new HumanClicks(page,mouse,personality);
    typing = new HumanTyping(page, personality);

    System.out.println("Browser launched successfully.");
    }

    public void openApplication() {

        
        System.out.println("Opening application...");

        page.navigate(BASE_URL);

    }

    public void clickViewDemo() throws InterruptedException {

    if (HumanBehavior.chance(20)) {
    clicks.doubleClick("h1");
}
    System.out.println("Clicking View Demo...");

    mouse.moveToElement("#view-demo-button");

    clicks.click("#view-demo-button");

}
public void waitForDemoPage() {

    System.out.println("Waiting for Demo page...");

    page.waitForURL("**/demo");

    System.out.println("Demo page loaded.");
}
    
    public void closeBrowser() {

    System.out.println("Closing browser...");

    if (context != null)
        context.close();

    if (browser != null)
        browser.close();

    if (playwright != null)
        playwright.close();
    }

    public void fillName() throws InterruptedException {

    System.out.println("Entering name...");

    mouse.moveToElement("#user-name");

    typing.type(
        "#user-name",
        sessionName
);

}

    public void selectRating() throws InterruptedException {

    System.out.println("Selecting rating...");

    mouse.moveToElement("#rating-" + sessionRating);

    clicks.click("#rating-" + sessionRating);

}

    public void fillFeedback() throws InterruptedException {

    System.out.println("Entering feedback...");

    mouse.moveToElement("#feedback");

    typing.type(
        "#feedback",
        sessionFeedback
);

}

    public void submitFeedback() throws InterruptedException {
    

    System.out.println("Submitting feedback...");

    mouse.moveToElement("#submit-feedback");
    if (HumanBehavior.chance(30)) {
    clicks.rightClick("body");
}

    clicks.click("#submit-feedback");

}

    public void clickAnalyze() throws InterruptedException {

    System.out.println("Analyzing behavior...");

    mouse.moveToElement("#analyze-button");
    if (HumanBehavior.chance(20)) {
    clicks.doubleClick("body");
}


    clicks.click("#analyze-button");

}
private void initializeSessionData() {

    sessionName =
            NAMES[ThreadLocalRandom.current().nextInt(NAMES.length)];

    sessionFeedback =
            FEEDBACKS[ThreadLocalRandom.current().nextInt(FEEDBACKS.length)];

    sessionRating =
            RATINGS[ThreadLocalRandom.current().nextInt(RATINGS.length)];

}

}