package com.passivecaptcha.bot;

import com.microsoft.playwright.*;
import com.passivecaptcha.bot.botd.util.behavior.*;
import com.passivecaptcha.bot.botd.util.brain.*;
import com.passivecaptcha.bot.botd.util.pages.DemoPage;
import java.util.concurrent.ThreadLocalRandom;

public class BotD {

    private static final String BASE_URL = "http://localhost:3000";

    private static final int TOTAL_SESSIONS = 50;

    private static final String[] NAMES = {

    "Aarav Sharma",
    "Vihaan Patel",
    "Aditya Kashyap",
    "Rohan Patil",
    "Karan Mehta",
    "Rahul Singh",
    "Utkarsh Joshilkar",
    "Aniket Deshmukh",
    "Siddharth Kulkarni",
    "Yash More",
    "Atharva Joshi",
    "Pranav Patil",
    "Omkar Jadhav",
    "Harsh Vyas",
    "Aman Verma",
    "Arjun Nair",
    "Ritesh Kumar",
    "Manav Shah",
    "Tushar Gupta",
    "Soham Kulkarni",
    "Aditi Patil",
    "Sneha Majge",
    "Neha Joshi",
    "Priya Patil",
    "Ananya Sharma",
    "Ishita Verma",
    "Vaidehi Patil",
    "Pooja Kulkarni",
    "Riya Shah",
    "Khushi Mehta",
    "Shruti Patil",
    "Sakshi Gupta",
    "Nikita Sharma",
    "Anjali Singh",
    "Tanvi More",
    "Rutuja Deshmukh",
    "Pallavi Patil",
    "Simran Kaur",
    "Meera Nair",
    "Vaishnavi Kulkarni",
    "Dev Patel",
    "Krishna Yadav",
    "Ayush Mishra",
    "Rohit Chavan",
    "Abhishek Jain",
    "Mayur Pawar",
    "Akash Shinde",
    "Nikhil Patil",
    "Saurabh Singh",
    "Harsh Patel"

};
    private static final String[] FEEDBACKS = {

    "The application is easy to use and navigation feels smooth.",
    "I found the interface clean and simple to understand.",
    "Everything worked as expected without any issues.",
    "The overall experience was pleasant and responsive.",
    "The layout is intuitive and user friendly.",
    "The form was straightforward and easy to complete.",
    "The response time of the application is impressive.",
    "I like the simple design and clean interface.",
    "Everything loaded quickly and performed well.",
    "The demo explained the workflow clearly.",
    "The application feels polished and responsive.",
    "Navigation between pages was seamless.",
    "The interface is modern and visually appealing.",
    "The overall interaction was smooth.",
    "The application was simple to understand.",
    "The user experience is quite good.",
    "The form submission worked without delay.",
    "The website is easy to navigate.",
    "The interaction feels natural and responsive.",
    "The application performs consistently well.",
    "Everything was easy to locate.",
    "The workflow is clear and intuitive.",
    "The interface is well organized.",
    "The buttons respond quickly.",
    "The design looks professional.",
    "I enjoyed using this application.",
    "The experience was comfortable.",
    "The demo page worked perfectly.",
    "The application is responsive across actions.",
    "Overall it provides a good user experience.",
    "The feedback form is easy to use.",
    "Everything behaved as expected.",
    "The design is neat and uncluttered.",
    "The application feels reliable.",
    "The interface is simple but effective.",
    "I appreciate the clean layout.",
    "The navigation is fast and smooth.",
    "The interaction is intuitive.",
    "The application is well designed.",
    "Overall I am satisfied with the experience."

};

    private static final int[] RATINGS = {

    5,5,5,5,5,5,5,5,5,5,

    4,4,4,4,4,4,4,4,

    3,3,3,

    2,

    1

};

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    private Brain brain;

    private Memory memory;

    private BrainCycle brainCycle;

    private BehaviorEngine behavior;

    private HumanMouse mouse;

    private HumanClick click;

    private HumanScroll scroll;

    private HumanTyping typing;
    
    public static void main(String[] args) {

    for (int session = 1; session <= TOTAL_SESSIONS; session++) {

        System.out.println("\n==================================================");
        System.out.println("               BOT D SESSION");
        System.out.println("                 " + session + " / " + TOTAL_SESSIONS);
        System.out.println("==================================================");

        long startTime = System.currentTimeMillis();

        BotD bot = new BotD();

        try {

            bot.initialize();

            String sessionName = bot.randomName();
            String sessionFeedback = bot.randomFeedback();
            int sessionRating = bot.randomRating();

            System.out.println("------------------------------------------");
            System.out.println("Brain State");
            System.out.println("------------------------------------------");
            System.out.println("Personality : " + bot.memory.getPersonality());
            System.out.println("Confidence  : " + bot.memory.getConfidence());
            System.out.println("Attention   : " + bot.memory.getAttention());

            System.out.println("------------------------------------------");
            System.out.println("Selected Parameters");
            System.out.println("------------------------------------------");
            System.out.println("Name       : " + sessionName);
            System.out.println("Rating     : " + sessionRating);
            System.out.println("Feedback   : " + sessionFeedback);

            System.out.println("------------------------------------------");
            System.out.println("Starting Session...");
            System.out.println("------------------------------------------");

            bot.runSession(
                    sessionName,
                    sessionFeedback,
                    sessionRating
            );

            long endTime = System.currentTimeMillis();

            System.out.println("------------------------------------------");
            System.out.println("SESSION COMPLETED");
            System.out.println("Execution Time : "
                    + ((endTime - startTime) / 1000.0)
                    + " sec");
            System.out.println("------------------------------------------");

        } catch (Exception e) {

            System.out.println("------------------------------------------");
            System.out.println("SESSION FAILED");
            System.out.println("------------------------------------------");

            e.printStackTrace();

        } finally {

            bot.close();

            try {

                Thread.sleep(3000);

            } catch (InterruptedException ignored) {
            }
        }
    }

    System.out.println("\n==================================================");
    System.out.println("      ALL BOT D SESSIONS COMPLETED");
    System.out.println("==================================================");
}


private void initialize() {

    System.out.println("Initializing Bot D...");

    Personality personality =
            Personality.values()[
                    ThreadLocalRandom.current()
                            .nextInt(Personality.values().length)
            ];

    brain = new Brain();

    memory = new Memory(personality);

    brainCycle = new BrainCycle(memory);

    behavior = new BehaviorEngine(
            brain,
            memory,
            brainCycle
    );

    System.out.println("Personality Selected : " + personality);

    playwright = Playwright.create();

    browser = playwright.chromium().launch(

            new BrowserType.LaunchOptions()

                    .setHeadless(false)

    );

    context = browser.newContext(

            new Browser.NewContextOptions()

                    .setViewportSize(1366, 768)

    );

    page = context.newPage();

    mouse = new HumanMouse(page, memory);

    click = new HumanClick(page, memory, mouse);

    scroll = new HumanScroll(page, memory);

    typing = new HumanTyping(page, memory);

    System.out.println("------------------------------------------");
    System.out.println("Brain Initialized");
    System.out.println("Current Goal : " + brain.getCurrentGoal());
    System.out.println("------------------------------------------");

    brain.setCurrentGoal(
        Goal.OPEN_APPLICATION
);
}

private void runSession(String name,
                        String feedback,
                        int rating) throws Exception {

    System.out.println("\nOpening Application...");
    page.navigate(BASE_URL);

    // -----------------------------
    // Goal : Open Demo
    // -----------------------------
    brain.setCurrentGoal(Goal.OPEN_DEMO);

    System.out.println("Current Goal : " + brain.getCurrentGoal());

    behavior.think();

    click.click(DemoPage.VIEW_DEMO);

    page.waitForURL("**/demo");

    System.out.println("Goal Completed : OPEN_DEMO");

    System.out.println(
        "Brain Thinking : " + brain.isThinking()
);

    // -----------------------------
    // Goal : Enter Name
    // -----------------------------
    brain.setCurrentGoal(Goal.ENTER_NAME);

    System.out.println("Current Goal : " + brain.getCurrentGoal());

    behavior.think();

    typing.type(
            DemoPage.NAME,
            name
    );

    System.out.println("Goal Completed : ENTER_NAME");

    System.out.println(
        "Brain Thinking : " + brain.isThinking()
);

    // -----------------------------
    // Goal : Rating
    // -----------------------------
    brain.setCurrentGoal(Goal.SELECT_RATING);

    System.out.println("Current Goal : " + brain.getCurrentGoal());

    behavior.think();

    click.click(
            DemoPage.rating(rating)
    );

    System.out.println("Goal Completed : SELECT_RATING");

    System.out.println(
        "Brain Thinking : " + brain.isThinking()
);

    // -----------------------------
    // Goal : Feedback
    // -----------------------------
    brain.setCurrentGoal(Goal.ENTER_FEEDBACK);

    System.out.println("Current Goal : " + brain.getCurrentGoal());

    behavior.think();

    typing.type(
            DemoPage.FEEDBACK,
            feedback
    );

    System.out.println("Goal Completed : ENTER_FEEDBACK");

    System.out.println(
        "Brain Thinking : " + brain.isThinking()
);

    // -----------------------------
    // Goal : Submit
    // -----------------------------
    brain.setCurrentGoal(Goal.SUBMIT_FEEDBACK);

    System.out.println("Current Goal : " + brain.getCurrentGoal());

    behavior.think();

    click.click(
            DemoPage.SUBMIT
    );

    System.out.println("Goal Completed : SUBMIT_FEEDBACK");

    System.out.println(
        "Brain Thinking : " + brain.isThinking()
);

    // -----------------------------
    // Goal : Analyze
    // -----------------------------
    brain.setCurrentGoal(Goal.ANALYZE);

    System.out.println("Current Goal : " + brain.getCurrentGoal());

    behavior.think();

    page.locator(DemoPage.ANALYZE).scrollIntoViewIfNeeded();

Thread.sleep(800);

    click.click(
            DemoPage.ANALYZE
    );

    System.out.println("Goal Completed : ANALYZE");

    System.out.println(
        "Brain Thinking : " + brain.isThinking()
);

    brain.setCurrentGoal(
            Goal.FINISHED
    );

    System.out.println("Current Goal : " + brain.getCurrentGoal());

    page.waitForTimeout(3000);
}
private String randomName() {

    return NAMES[
            ThreadLocalRandom.current()
                    .nextInt(NAMES.length)
    ];
}

private String randomFeedback() {

    return FEEDBACKS[
            ThreadLocalRandom.current()
                    .nextInt(FEEDBACKS.length)
    ];
}

private int randomRating() {

    return RATINGS[
            ThreadLocalRandom.current()
                    .nextInt(RATINGS.length)
    ];
}

private void close() {

    System.out.println("------------------------------------------");
    System.out.println("Closing Browser");
    System.out.println("------------------------------------------");

    try {

        if (context != null) {
            context.close();
        }

    } catch (Exception ignored) {
    }

    try {

        if (browser != null) {
            browser.close();
        }

    } catch (Exception ignored) {
    }

    try {

        if (playwright != null) {
            playwright.close();
        }

    } catch (Exception ignored) {
    }

    System.out.println("Browser Closed Successfully");
}
}