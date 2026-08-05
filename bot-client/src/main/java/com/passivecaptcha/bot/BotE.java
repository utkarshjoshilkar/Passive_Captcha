package com.passivecaptcha.bot;

import com.microsoft.playwright.Page;
import com.passivecaptcha.bot.bote.util.browser.*;
import com.passivecaptcha.bot.bote.util.behavior.*;
import com.passivecaptcha.bot.bote.util.brain.*;
import com.passivecaptcha.bot.bote.util.pages.Demopage;

import java.util.concurrent.ThreadLocalRandom;

public class BotE {

    private static final String BASE_URL = "http://localhost:3000";

   private static final String[] NAMES = {
    "Aarav",
    "Vihaan",
    "Aditya",
    "Rohan",
    "Karan",
    "Neha",
    "Priya",
    "Sneha",
    "Rahul",
    "Utkarsh",

    "Aditi Sharma",
    "Rohan Patil",
    "Ananya Kulkarni",
    "Siddharth Joshi",
    "Vaishnavi Deshmukh",
    "Prathamesh Jadhav",
    "Shreyas Kulkarni",
    "Ishita Mehta",
    "Aryan Singh",
    "Tanvi Patil",

    "Arjun Rajesh Kumar",
    "Meera Sanjay Kulkarni",
    "Krishna Prakash Nair",
    "Nikhil Ashok Patwardhan",
    "Shravani Vinod Deshpande",
    "Saurabh Vijay Chavan",
    "Rutuja Mahesh Patil",
    "Harsh Vikas Sharma",
    "Aman Rajput",
    "Pooja Sharma",

    "Mohammed Ayaan Khan",
    "John Mathew",
    "Emily Johnson",
    "Alexander Thomas Brown",
    "Sophia Williams",
    "Daniel James Smith",
    "Olivia Grace Wilson",
    "Michael Anderson",
    "Isabella Garcia",
    "Christopher David Miller"
};

    private static final String[] FEEDBACKS = {

    "The interface is clean and easy to navigate every time.",

    "Everything worked smoothly without any noticeable issues.",

    "The application is simple, responsive, and easy to use.",

    "I liked the overall experience and the fast response time.",

    "Navigation was smooth and all the features worked properly.",

    "The design looks modern and the interface feels intuitive.",

    "Submitting the feedback was quick and straightforward.",

    "The application performed well throughout the entire session.",

    "Everything loaded quickly and the experience was pleasant.",

    "The workflow is simple and easy to understand for everyone.",

    "The controls responded quickly and the interface looked clean.",

    "I completed the form without facing any problems or delays.",

    "Overall the application provides a smooth user experience.",

    "The layout is organized well and everything is easy to find.",

    "The website feels responsive and performs consistently well.",

    "The feedback form was simple and required very little effort.",

    "The application was reliable and every feature worked correctly.",

    "I found the interface comfortable and easy ",

    "The overall performance met my expectations during this session.",

    "The experience was enjoyable because everything worked as expected.",

    "The application is user friendly and the navigation feels natural.",

    "Every page responded quickly and all buttons worked correctly.",

    "The interface is visually appealing and easy to understand.",

    "Completing this task was simple because the workflow is clear.",

    "Everything behaved consistently from start to finish.",

    "The application offers a pleasant.",

    "The overall design is clean and the controls are responsive.",

    "The application feels polished and performs efficiently.",

    "Using this application was simple and completely hassle free.",

    "I am satisfied with the overall usability and performance."

};

    private static final int[] RATINGS = {
            3, 4, 5
    };

    private Brain brain;

private Memory memory;

private GoalManager goalManager;

private DecisionEngine decisionEngine;

private BrainCycle brainCycle;

    private BrowserManager browserManager;
    private BrowserProfileManager profileManager;

    private BrowserProfile browserProfile;
    private FingerprintProfile fingerprintProfile;

    private Page page;

    private SessionPersonality personality;

    private HumanMouseV2 mouse;
    private HumanClickV2 click;
    private HumanScrollV2 scroll;
    private HumanTypingV2 typing;

    public BotE() {

        profileManager = new BrowserProfileManager();

        browserProfile =
                profileManager.randomBrowserProfile();

        fingerprintProfile =
                profileManager.randomFingerprint();

        personality =
                SessionPersonality.random();

        browserManager =
                new BrowserManager();
    }

    public static void main(String[] args) {

        for (int session = 1; session <= 60; session++) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("BOT E SESSION " + session + " OF 60");
            System.out.println("=================================");

            BotE bot = new BotE();

            try {

                bot.initialize();

                bot.runSession();

                System.out.println("Session "
                        + session
                        + " completed.");

            } catch (Exception e) {

                System.out.println("Session "
                        + session
                        + " failed.");

                e.printStackTrace();

            } finally {

                bot.close();

                try {

                    Thread.sleep(3000);

                } catch (InterruptedException ignored) {
                }
            }
        }

        System.out.println();
        System.out.println("===============================");
        System.out.println("ALL BOT E SESSIONS COMPLETED");
        System.out.println("===============================");
    }

    private void runSession() throws Exception {
        String name = NAMES[ThreadLocalRandom.current().nextInt(NAMES.length)];
        String feedback = FEEDBACKS[ThreadLocalRandom.current().nextInt(FEEDBACKS.length)];
        int rating = RATINGS[ThreadLocalRandom.current().nextInt(RATINGS.length)];

        System.out.println("Personality : " + personality);

        System.out.println("Name : " + name);

        System.out.println("Rating : " + rating);

        System.out.println("Navigating to " + BASE_URL);
        page.navigate(BASE_URL);
        brainThink();
        click.click(Demopage.VIEW_DEMO);
        goalManager.completeCurrentGoal();
        page.waitForURL("**/demo");
        brainThink();

        typing.type(Demopage.NAME, name);
        goalManager.completeCurrentGoal();
        brainThink();
        click.click(Demopage.rating(rating));
        goalManager.completeCurrentGoal();
        brainThink();
        typing.type(Demopage.FEEDBACK, feedback);
        goalManager.completeCurrentGoal();
       brainThink();
        click.click(Demopage.SUBMIT);
        goalManager.completeCurrentGoal();
        brainThink();

        scroll.scrollTo(Demopage.ANALYZE);
        goalManager.completeCurrentGoal();
        brainThink();
        click.click(Demopage.ANALYZE);
        goalManager.completeCurrentGoal();

        page.waitForTimeout(3000);
    }

    private void initialize() {

        brain = new Brain();

memory = new Memory();

goalManager = new GoalManager(brain);

decisionEngine =
        new DecisionEngine(
                brain,
                memory,
                goalManager
        );

brainCycle =
        new BrainCycle(memory);

        browserManager.launch(
                browserProfile,
                fingerprintProfile
        );

        page = browserManager.getPage();

        mouse =
                new HumanMouseV2(page, personality);

        click =
                new HumanClickV2(page, mouse, personality);

        scroll =
                new HumanScrollV2(page, personality);

        typing =
                new HumanTypingV2(page, personality);
    }

    private void close() {
        if (browserManager != null) {
            browserManager.close();
        }
    }
    private void brainThink() throws InterruptedException {

    System.out.println(
            "Goal : "
                    + goalManager.currentGoal()
    );

    Decision decision =
            decisionEngine.nextDecision();

    switch (decision) {

        case PAUSE ->

                Thread.sleep(random(400,900));

        case VERIFY ->

                Thread.sleep(random(200,450));

        case ACT -> {
        }
    }

    for (CognitiveState state :
            brainCycle.nextCycle()) {

        switch (state) {

            case LOOK ->

                    Thread.sleep(random(80,180));

            case READ ->

                    Thread.sleep(random(150,350));

            case THINK ->

                    Thread.sleep(random(300,600));

            case VERIFY ->

                    Thread.sleep(random(150,250));

            case PAUSE ->

                    Thread.sleep(random(250,600));

            case ACT -> {
            }
        }
    }
}
private int random(int min, int max) {

    return ThreadLocalRandom.current()
            .nextInt(min, max);
}
}