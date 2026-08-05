package com.passivecaptcha.bot;

import com.passivecaptcha.bot.botc.util.BehaviorEngine;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v138.emulation.Emulation;
import org.openqa.selenium.devtools.v138.network.Network;
import org.openqa.selenium.devtools.v138.performance.Performance;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class BotC {

    private static final String BASE_URL = "http://localhost:3000";

    private static final String[] NAMES = {
            "Aarav jhah",
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
            "The application is easy to use. I like it.",
            "Navigation is smooth and intuitive.",
            "The demo works well. I like its interface.",
            "Overall a pleasant experience.",
            "Everything responded quickly.",
            "The interface looks clean and user friendly.",
            "The form was simple to complete.",
            "Good design and responsiveness.",
            "Nice interaction with the application.",
            "The demo was informative."
    };

    private static final int[] RATINGS = {
            3, 4, 5, 4, 5, 3, 4, 5
    };

    private static final String[] USER_AGENTS = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.7204.184 Safari/537.36",
            "Mozilla/5.0 (Windows NT 11.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.7204.184 Safari/537.36",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.7204.184 Safari/537.36"
    };

    private static final String CHROME_BINARY =
            "D:/software/chrome-win64/chrome-win64/chrome.exe";

    private static final String CHROMEDRIVER_PATH =
            "D:/software/chromedriver-win64/chromedriver.exe";

    private static final String BOT_FAMILY = "BotC";
    private static final String BOT_VERSION = "V4";

    private ChromeDriver driver;
    private DevTools devTools;
    private BehaviorEngine behaviorEngine;

    private String sessionName;
    private String sessionFeedback;
    private int sessionRating;
    private String sessionUserAgent;

    public static void main(String[] args) {

        final int TOTAL_SESSIONS = 20;

        for (int session = 1; session <= TOTAL_SESSIONS; session++) {

            System.out.println("=================================");
            System.out.println(BOT_FAMILY + " " + BOT_VERSION);
            System.out.println("Session " + session + "/" + TOTAL_SESSIONS);
            System.out.println("=================================");

            BotC bot = new BotC();

            bot.initializeSessionData();

            try {
                Thread.sleep(10000);

                bot.launchBrowser();

                bot.openApplication();

                bot.clickViewDemo();

                bot.waitForDemoPage();

                bot.getBehaviorEngine().read();

                bot.fillName();

                bot.selectRating();

                bot.fillFeedback();

                bot.getBehaviorEngine().think();

                bot.submitFeedback();

                bot.getBehaviorEngine().read();

                bot.clickAnalyze();

                bot.getBehaviorEngine().idle();

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                bot.closeBrowser();
            }
        }
    }

    private void initializeSessionData() {

        sessionUserAgent =
                USER_AGENTS[ThreadLocalRandom.current().nextInt(USER_AGENTS.length)];

        sessionName =
                NAMES[ThreadLocalRandom.current().nextInt(NAMES.length)];

        sessionFeedback =
                FEEDBACKS[ThreadLocalRandom.current().nextInt(FEEDBACKS.length)];

        sessionRating =
                RATINGS[ThreadLocalRandom.current().nextInt(RATINGS.length)];
    }

    private void overrideUserAgent() {

        System.out.println("--------------------------------");
        System.out.println("Applying User Agent...");
        System.out.println("--------------------------------");

        devTools.send(
                Emulation.setUserAgentOverride(
                        sessionUserAgent,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
        );

        System.out.println("User Agent Applied.");
    }

    private void launchBrowser() {

        System.out.println("--------------------------------");
        System.out.println("Launching Chrome...");
        System.out.println("--------------------------------");

        System.setProperty(
                "webdriver.chrome.driver",
                CHROMEDRIVER_PATH
        );

        ChromeOptions options = new ChromeOptions();
        options.setBinary(CHROME_BINARY);
        options.addArguments("--window-size=1600,1200");

        driver = new ChromeDriver(options);

        behaviorEngine = new BehaviorEngine(driver);

        devTools = driver.getDevTools();

        devTools.createSession();

        devTools.send(
                Network.enable(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
        );

        devTools.send(
                Performance.enable(Optional.empty())
        );

        overrideUserAgent();

        driver.manage().window().setSize(
                new Dimension(1600, 1200)
        );

        driver.manage().window().setPosition(
                new Point(0, 0)
        );

        System.out.println("Chrome launched.");
        System.out.println("CDP Session Created.");
        System.out.println("Network Enabled.");
        System.out.println("Performance Enabled.");
    }

    private void openApplication() {

        System.out.println("--------------------------------");
        System.out.println("Opening Application...");
        System.out.println("--------------------------------");

        driver.get(BASE_URL);

        getBehaviorEngine().read();

        System.out.println("Application Loaded.");
    }

    private void clickViewDemo() {

        System.out.println("--------------------------------");
        System.out.println("Clicking View Demo...");
        System.out.println("--------------------------------");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );

        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("view-demo-button")
                )
        );

        behaviorEngine.click(button);
    }

    private void waitForDemoPage() {

        new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        ).until(
                ExpectedConditions.urlContains("/demo")
        );

        getBehaviorEngine().read();

        System.out.println("Demo page loaded.");
    }

    private void fillName() {

        System.out.println("--------------------------------");
        System.out.println("Entering Name...");
        System.out.println("--------------------------------");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement nameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("user-name")
                )
        );

        behaviorEngine.type(
                nameField,
                sessionName
        );

        behaviorEngine.think();
    }

    private void selectRating() {

        System.out.println("--------------------------------");
        System.out.println("Selecting Rating...");
        System.out.println("--------------------------------");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement rating = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("rating-" + sessionRating)
                )
        );

        behaviorEngine.click(rating);

        behaviorEngine.read();
    }

    private void fillFeedback() {

        System.out.println("--------------------------------");
        System.out.println("Entering Feedback...");
        System.out.println("--------------------------------");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement feedback = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("feedback")
                )
        );

        behaviorEngine.type(
                feedback,
                sessionFeedback
        );

        behaviorEngine.read();
    }

    private void submitFeedback() {

        System.out.println("--------------------------------");
        System.out.println("Submitting Feedback...");
        System.out.println("--------------------------------");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement submit = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("submit-feedback")
                )
        );

        behaviorEngine.click(submit);

        try {

            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.alertIsPresent())
                    .accept();

            System.out.println("Alert accepted.");

        } catch (Exception ignored) {

        }

        behaviorEngine.idle();
    }

    private void clickAnalyze() {

        System.out.println("--------------------------------");
        System.out.println("Clicking Analyze...");
        System.out.println("--------------------------------");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement analyze = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("analyze-button")
                )
        );

        behaviorEngine.click(analyze);

        behaviorEngine.read();
    }

    private void closeBrowser() {

        System.out.println("--------------------------------");
        System.out.println("Closing Browser...");
        System.out.println("--------------------------------");

        try {

            if (devTools != null) {
                devTools.close();
            }

        } catch (Exception e) {

            System.out.println("DevTools already closed.");

        }

        try {

            if (driver != null) {
                driver.quit();
            }

        } catch (Exception e) {

            System.out.println("Driver already closed.");

        }

        System.out.println("Browser closed.");
    }

    /*
     * ==========================
     * Getters
     * ==========================
     */

    public BehaviorEngine getBehaviorEngine() {
        return behaviorEngine;
    }

}