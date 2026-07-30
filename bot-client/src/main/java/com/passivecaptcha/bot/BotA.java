package com.passivecaptcha.bot;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class BotA {

    private static final String BASE_URL = "http://localhost:3000";

    private static final int WAIT_TIMEOUT = 10;

    private static final String BOT_NAME = "John Doe";

    private static final String BOT_FEEDBACK =
            "The Passive CAPTCHA interface is responsive and the interaction feels smooth.";

    private WebDriver driver;

    private WebDriverWait wait;

    public static void main(String[] args) {
       final int TOTAL_RUNS = 60;

    for (int i = 1; i <= TOTAL_RUNS; i++) {

        System.out.println("====================================");
        System.out.println("Starting BotA V2 Session " + i + "/" + TOTAL_RUNS);
        System.out.println("====================================");


    BotA bot = new BotA();

    try {

        bot.launchBrowser();

        bot.openApplication();

        bot.clickViewDemo();

        bot.waitForDemoPage();
        bot.sleepRandom(1500, 3500);

        // Small mouse movement before interacting
        bot.performMouseMovement();
        bot.performScrolling();
        bot.sleepRandom(800, 1800);

        // Fill the form first
        bot.fillName();

        bot.selectRating();
        bot.sleepRandom(700, 2000);

        bot.fillFeedback();

        // Now perform a small scroll
        bot.performScrolling();

        // Additional mouse interactions
        bot.performDoubleClick();

        bot.performRightClick();
        bot.sleepRandom(1500, 4000);

        // Submit feedback
        bot.submitFeedback();

        // Give React/UI time to settle after alert
        bot.sleepRandom(2500, 5000);
        bot.performScrolling();     
        // Analyze behavior
        bot.clickAnalyze();

        bot.waitForAnalysisResult();
         System.out.println("Session " + i + " completed successfully.");

    } catch (Exception e) {

        System.err.println("BotA V1 - Session " + i + " failed.");
        e.printStackTrace();

    } finally {

        bot.closeBrowser();

    }

     try {
            Thread.sleep(10000); // 5 seconds
        } catch (InterruptedException ignored) {
        }
    }

    System.out.println("All 60 BotA V1 sessions completed.");
}
   /**
 * Launches Chrome browser and initializes WebDriverWait.
 */
public void launchBrowser() {

    System.out.println("\n====================================");
    System.out.println("Launching Browser...");
    System.out.println("====================================");

    WebDriverManager.chromedriver().setup();

    driver = new ChromeDriver();

    driver.manage().window().maximize();

    wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT));
}


/**
 * Opens the Passive CAPTCHA application.
 */
public void openApplication() {

    System.out.println("Opening application...");

    driver.get(BASE_URL);

}


/**
 * Clicks the View Demo button from the Home page.
 */
public void clickViewDemo() {

    System.out.println("Navigating to Demo page...");

    WebElement demoButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id("view-demo-button")
            )
    );

    demoButton.click();
    sleepRandom(300,600);

}


/**
 * Wait until Demo page is fully loaded.
 */
public void waitForDemoPage() {

    System.out.println("Waiting for Demo page...");

    wait.until(ExpectedConditions.urlContains("/demo"));

    wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.id("user-name")
            )
    );

    System.out.println("Demo page loaded successfully.\n");

}


/**
 * Returns a random delay between min and max milliseconds.
 */
private int randomDelay(int min, int max) {

    return ThreadLocalRandom.current().nextInt(min, max + 1);

}


/**
 * Sleeps for a random amount of time.
 */
private void sleepRandom(int min, int max) {

    try {

        Thread.sleep(randomDelay(min, max));

    } catch (InterruptedException e) {

        Thread.currentThread().interrupt();

    }

}
    /**
 * Performs mouse movement across the page.
 *
 * Behavioral features generated:
 * - Mouse movement count
 * - Total pointer distance
 * - Speed
 * - Acceleration
 * - Jerk
 * - Direction changes
 * - Hesitation
 */
public void performMouseMovement() {

    System.out.println("Performing mouse movement...");

    Actions actions = new Actions(driver);

    actions
            .moveByOffset(180, 120)
            .pause(Duration.ofMillis(randomDelay(150, 700)))

            .moveByOffset(120, -40)
            .pause(Duration.ofMillis(randomDelay(150, 700)))

            .moveByOffset(-90, 160)
            .pause(Duration.ofMillis(randomDelay(150, 700)))

            .moveByOffset(-130, -80)
            .pause(Duration.ofMillis(randomDelay(150, 700)))

            .moveByOffset(80, 40)
            .pause(Duration.ofMillis(randomDelay(150, 700)))

            .perform();

    sleepRandom(300,600);

}
/**
 * Scrolls down and up.
 *
 * Behavioral features generated:
 * - Scroll count
 * - Scroll distance
 * - Scroll speed
 * - Scroll pause
 * - Direction changes
 */
public void performScrolling() {

    System.out.println("Scrolling page...");

    JavascriptExecutor js = (JavascriptExecutor) driver;

    js.executeScript("window.scrollBy(0,450);");

    sleepRandom(200,900);

    js.executeScript("window.scrollBy(0,300);");

    sleepRandom(150,800);

    js.executeScript("window.scrollBy(0,-250);");

    sleepRandom(150,700);

    js.executeScript("window.scrollBy(0,-500);");

    sleepRandom(200,900);


}
/**
 * Types text character-by-character with random delays.
 *
 * Behavioral features generated:
 * - Key press count
 * - Typing speed
 * - Average key interval
 * - Typing rhythm
 */
private void typeSlowly(WebElement element, String text) {

    int typed = 0;
    int pauseAfter = randomDelay(5, 8);

    for (char ch : text.toCharArray()) {

        element.sendKeys(String.valueOf(ch));

        sleepRandom(40, 220);

        typed++;

        if (typed == pauseAfter) {

            // Small thinking pause
            sleepRandom(300, 900);

            typed = 0;
            pauseAfter = randomDelay(5, 8);
        }
    }
}
   /**
 * Fills the Name field.
 */
public void fillName() {

    System.out.println("Entering name...");

    WebElement nameField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.id("user-name")
            )
    );

    // Bring the field into the center of the screen
    ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            nameField
    );

    sleepRandom(300,500);

    nameField.click();

    sleepRandom(150,300);

    nameField.clear();

    sleepRandom(120,250);

    typeSlowly(nameField, BOT_NAME);

    sleepRandom(250,500);

}

    /**
 * Selects a 4-star rating.
 */
public void selectRating() {

    System.out.println("Selecting rating...");

    WebElement rating = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id("rating-4")
            )
    );

    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});",
        rating
);

sleepRandom(300,500);

rating.click();

    sleepRandom(250,450);

}

   /**
 * Types feedback into the textarea.
 */
public void fillFeedback() {

    System.out.println("Typing feedback...");

    WebElement feedback = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id("feedback")
            )
    );
((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});",
        feedback
);

sleepRandom(300,500);

feedback.click();
    sleepRandom(200,400);

    feedback.clear();

    sleepRandom(150,300);

    typeSlowly(feedback, BOT_FEEDBACK);

    sleepRandom(300,600);

}
   /**
 * Performs a double click inside the feedback box.
 *
 * Behavioral features generated:
 * - Double click count
 * - Click interval
 */
public void performDoubleClick() {

    System.out.println("Performing double click...");

    WebElement feedback = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.id("feedback")
            )
    );

    sleepRandom(200,400);

    new Actions(driver)
            .doubleClick(feedback)
            .perform();

    sleepRandom(300,500);

}


/**
 * Performs a right click inside the feedback box.
 *
 * Behavioral features generated:
 * - Right click count
 * - Context menu event
 */
public void performRightClick() {

    System.out.println("Performing right click...");

    WebElement feedback = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.id("feedback")
            )
    );

    sleepRandom(250,500);

    new Actions(driver)
            .contextClick(feedback)
            .perform();

    sleepRandom(300,500);

}

   /**
 * Clicks Submit Feedback and accepts the browser alert.
 */
public void submitFeedback() {

    System.out.println("Submitting feedback...");

    WebElement submitButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id("submit-feedback")
            )
    );

   ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});",
        submitButton
);

sleepRandom(300,500);

submitButton.click();

    try {

        Alert alert = wait.until(
                ExpectedConditions.alertIsPresent()
        );

        System.out.println("Alert detected.");

        sleepRandom(300,500);

        alert.accept();

        System.out.println("Alert accepted.");

    }

    catch (Exception ignored) {

        System.out.println("No alert appeared.");

    }

    sleepRandom(400,700);

}

public void clickAnalyze() {

    System.out.println("Clicking Analyze button...");

    WebElement analyzeButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id("analyze-button")
            )
    );

    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});",
        analyzeButton
);

sleepRandom(1500, 2000);

analyzeButton.click();
    sleepRandom(1500, 2000);

    System.out.println("Analyze button clicked.");

}
    /**
 * Waits for the behavioral score to appear.
 */
public void waitForAnalysisResult() {

    System.out.println("Waiting for analysis result...");

    wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.className("score-result")
            )
    );

    System.out.println();
    System.out.println("====================================");
    System.out.println("Behavior Analysis Completed");
    System.out.println("====================================");
    System.out.println();

}

public void closeBrowser() {

    System.out.println("Closing browser...");

    if (driver != null) {

        driver.quit();

    }

    System.out.println("Bot execution finished.");

}
}