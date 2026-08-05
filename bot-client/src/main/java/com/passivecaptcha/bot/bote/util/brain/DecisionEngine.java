package com.passivecaptcha.bot.bote.util.brain;
import java.util.concurrent.ThreadLocalRandom;

public class DecisionEngine {

    private final Brain brain;
    private final Memory memory;
    private final GoalManager goalManager;

    public DecisionEngine(
            Brain brain,
            Memory memory,
            GoalManager goalManager
    ) {

        this.brain = brain;
        this.memory = memory;
        this.goalManager = goalManager;
    }

    public Decision nextDecision() {

        brain.startThinking();

        Decision decision;

        if (shouldVerify()) {

            decision = Decision.VERIFY;

        } else if (shouldPause()) {

            decision = Decision.PAUSE;

        } else {

            decision = Decision.ACT;
        }

        brain.stopThinking();

        return decision;
    }

    private boolean shouldPause() {

        int probability = 8;

        probability += memory.getFatigue() / 10;

        probability += (100 - memory.getAttention()) / 12;

        return ThreadLocalRandom.current()
                .nextInt(100) < probability;
    }

    private boolean shouldVerify() {

        int probability = 5;

        probability += (100 - memory.getConfidence()) / 10;

        return ThreadLocalRandom.current()
                .nextInt(100) < probability;
    }

    public Goal currentGoal() {

        return goalManager.currentGoal();
    }

    public void completeGoal() {

        goalManager.completeCurrentGoal();

        memory.sessionTick();
    }

}