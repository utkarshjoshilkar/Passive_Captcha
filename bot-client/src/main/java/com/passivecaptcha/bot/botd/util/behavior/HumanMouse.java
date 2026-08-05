package com.passivecaptcha.bot.botd.util.behavior;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.options.BoundingBox;
import com.passivecaptcha.bot.botd.util.brain.Memory;
import com.passivecaptcha.bot.botd.util.brain.Personality;
import java.util.concurrent.ThreadLocalRandom;

public class HumanMouse {

    private final Page page;
    private final Memory memory;

    // Current cursor position
    private double currentX;
    private double currentY;

    public HumanMouse(Page page, Memory memory) {

        this.page = page;
        this.memory = memory;

        // Start cursor at a realistic location
        currentX = ThreadLocalRandom.current().nextInt(150, 500);
        currentY = ThreadLocalRandom.current().nextInt(150, 400);
    }
    
    private double random(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    private int movementSteps() {

    return switch (memory.getPersonality()) {

        case FAST -> ThreadLocalRandom.current().nextInt(18,28);

        case NORMAL -> ThreadLocalRandom.current().nextInt(28,40);

        case CAREFUL -> ThreadLocalRandom.current().nextInt(40,60);
    };
}
public void moveTo(String selector) throws InterruptedException {

    Locator element = page.locator(selector);

    element.scrollIntoViewIfNeeded();

    BoundingBox box = element.boundingBox();

    if (box == null) {
        return;
    }

    lookAtTarget();

    hesitation();

    double targetX =
        box.x + box.width / 2.0
        + random(-3.0, 3.0);

double targetY =
        box.y + box.height / 2.0
        + random(-3.0, 3.0);

    moveTrajectory(targetX, targetY);

    correction(targetX, targetY);

    Thread.sleep(hoverTime());

handTremor();

    memory.reduceAttention(1);

    if (memory.getConfidence() < 90 &&
    ThreadLocalRandom.current().nextInt(100) < 30) {

    memory.increaseConfidence(1);
}
}
private void moveTrajectory(double targetX,
                            double targetY)
        throws InterruptedException {

    int steps = movementSteps();

    double curve = switch (memory.getPersonality()) {

    case FAST -> 60;

    case NORMAL -> 100;

    case CAREFUL -> 140;
};

double controlX =
        (currentX + targetX) / 2
        + random(-curve, curve);

double controlY =
        (currentY + targetY) / 2
        + random(-curve * 0.7, curve * 0.7);

    for (int i = 1; i <= steps; i++) {

        double t = i / (double) steps;

        // Quadratic Bezier

        double x =
                Math.pow(1 - t,2) * currentX
                + 2 * (1 - t) * t * controlX
                + Math.pow(t,2) * targetX;

        double y =
                Math.pow(1 - t,2) * currentY
                + 2 * (1 - t) * t * controlY
                + Math.pow(t,2) * targetY;

        // tiny human jitter
        x += random(-1.5,1.5);
        y += random(-1.5,1.5);
        if (i == steps / 2 &&
    ThreadLocalRandom.current().nextInt(100) < 8) {

    x += random(-15, 15);
    y += random(-15, 15);
}
page.mouse().move(
        x,
        y,
        new Mouse.MoveOptions()
                .setSteps(1)
);

        Thread.sleep(stepDelay(i,steps));
    }

    currentX = targetX;
    currentY = targetY;
}

private void correction(double targetX,
                        double targetY)
        throws InterruptedException {

    Personality personality = memory.getPersonality();

    int probability = switch (personality) {

        case FAST -> 8;

        case NORMAL -> 20;

        case CAREFUL -> 35;
    };

    if (ThreadLocalRandom.current().nextInt(100) >= probability) {
        return;
    }

    // Overshoot a few pixels
 double overX = targetX + random(-5, 5);
double overY = targetY + random(-5, 5);

    page.mouse().move(overX, overY);

    Thread.sleep(
            ThreadLocalRandom.current()
                    .nextInt(40, 90)
    );

    // Correct back to target
    page.mouse().move(targetX, targetY);

    Thread.sleep(
            ThreadLocalRandom.current()
                    .nextInt(50, 120)
    );

    currentX = targetX;
    currentY = targetY;
}
private int stepDelay(int step, int totalSteps) {

    double progress = step / (double) totalSteps;

    int confidence = memory.getConfidence();

    int base;

    if (progress < 0.20) {

        base = 10;

    } else if (progress < 0.80) {

        base = 4;

    } else {

        base = 10;
    }

    // Lower confidence → slightly slower movement
    base += (100 - confidence) / 12;

    return base + ThreadLocalRandom.current().nextInt(0, 4);
}
private void handTremor() throws InterruptedException {

    if (ThreadLocalRandom.current().nextInt(100) > 20) {
        return;
    }

    int shakes = ThreadLocalRandom.current().nextInt(1, 3);

    for (int i = 0; i < shakes; i++) {

        double tremorX = currentX + random(-1.5, 1.5);
double tremorY = currentY + random(-1.5, 1.5);

page.mouse().move(tremorX, tremorY);

        Thread.sleep(
                ThreadLocalRandom.current()
                        .nextInt(20, 50)
        );
    }
}
private int hoverTime() {

    Personality personality = memory.getPersonality();

    return switch (personality) {

        case FAST ->
                ThreadLocalRandom.current().nextInt(30,80);

        case NORMAL ->
                ThreadLocalRandom.current().nextInt(80,180);

        case CAREFUL ->
                ThreadLocalRandom.current().nextInt(180,350);
    };
}

private void lookAtTarget() throws InterruptedException {

    Personality personality = memory.getPersonality();

    int min;
    int max;

    switch (personality) {

        case FAST -> {
            min = 40;
            max = 120;
        }

        case NORMAL -> {
            min = 90;
            max = 180;
        }

        default -> {
            min = 180;
            max = 350;
        }
    }

    Thread.sleep(
            ThreadLocalRandom.current().nextInt(min, max)
    );
}

private void hesitation() throws InterruptedException {

    int confidence = memory.getConfidence();

    if (confidence < 50) {

        Thread.sleep(
                ThreadLocalRandom.current().nextInt(250,450)
        );

    } else {

        Thread.sleep(
                ThreadLocalRandom.current().nextInt(80,180)
        );
    }
}

}