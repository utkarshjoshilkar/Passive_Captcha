package com.passivecaptcha.bot.bote.util.behavior;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

import java.util.concurrent.ThreadLocalRandom;

public class HumanMouseV2 {

    private enum MovementStyle {

    DIRECT,
    CURVED,
    EXPLORER
}

    private final Page page;
    private final SessionPersonality personality;
    private final MovementStyle movementStyle;

    private double currentX;
    private double currentY;

    public HumanMouseV2(Page page, SessionPersonality personality) {
        this.page = page;
        this.personality = personality;

        MovementStyle[] styles = MovementStyle.values();

        this.movementStyle =
                styles[
                        ThreadLocalRandom.current()
                                .nextInt(styles.length)
                ];
        currentX = ThreadLocalRandom.current().nextInt(100, 500);
        currentY = ThreadLocalRandom.current().nextInt(100, 400);
    }

    public void moveTo(String selector) throws InterruptedException {

    Locator element = page.locator(selector);

    element.scrollIntoViewIfNeeded();

    BoundingBox box = element.boundingBox();

    if (box == null) {
        return;
    }

    double targetX =
            box.x + box.width / 2.0
            + random(-3, 3);

    double targetY =
            box.y + box.height / 2.0
            + random(-3, 3);

    double distance =
        distance(currentX, currentY, targetX, targetY);

int totalDuration =
        movementDuration(distance);

int steps =
        Math.max(12, totalDuration / 12);

    double curve = switch (movementStyle) {

    case DIRECT -> 30;

    case CURVED -> 90;

    case EXPLORER -> 140;
};

double controlX =
        (currentX + targetX) / 2
        + random(-curve, curve);

double controlY =
        (currentY + targetY) / 2
        + random(-curve * 0.7, curve * 0.7);

    for (int i = 1; i <= steps; i++) {

        double t = i / (double) steps;

        double x =
                Math.pow(1 - t, 2) * currentX
                + 2 * (1 - t) * t * controlX
                + Math.pow(t, 2) * targetX;

        double y =
                Math.pow(1 - t, 2) * currentY
                + 2 * (1 - t) * t * controlY
                + Math.pow(t, 2) * targetY;

        x += random(-1.2, 1.2);
        y += random(-1.2, 1.2);

        if (movementStyle == MovementStyle.EXPLORER &&
        i == steps / 2 &&
        chance(12)) {

    x += random(-20, 20);
    y += random(-20, 20);
}

if (movementStyle == MovementStyle.EXPLORER &&
        i == steps / 2 &&
        chance(12)) {

    x += random(-20, 20);
    y += random(-20, 20);
}

page.mouse().move(x, y);

        Thread.sleep(
        Math.max(
                2,
                totalDuration / steps
        )
        + randomInt(0, 3)
);
    }

    currentX = targetX;
    currentY = targetY;
}

    private int movementSteps() {
        return switch (personality) {
            case FAST -> ThreadLocalRandom.current().nextInt(15, 25);
            case NORMAL -> ThreadLocalRandom.current().nextInt(25, 40);
            case CAREFUL -> ThreadLocalRandom.current().nextInt(40, 60);
        };
    }
        private int stepDelay(int step, int totalSteps) {

    double progress = step / (double) totalSteps;

    int delay;

    if (progress < 0.20) {

        delay = 10;

    } else if (progress < 0.80) {

        delay = 4;

    } else {

        delay = 9;
    }

    return delay + randomInt(0, 4);
}
    private double distance(double x1,
                        double y1,
                        double x2,
                        double y2) {

    return Math.hypot(x2 - x1, y2 - y1);
}

    private double random(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private boolean chance(int percent) {
        return ThreadLocalRandom.current().nextInt(100) < percent;
    }

    private int movementDuration(double distance) {

    double factor = switch (personality) {

        case FAST -> 0.7;

        case NORMAL -> 1.0;

        case CAREFUL -> 1.3;
    };

    int duration = (int) (120 + distance * 0.35 * factor);

    return Math.min(900, Math.max(150, duration));
}
}