package com.passivecaptcha.bot.bote.util.brain;

public class GoalManager {

    private final Brain brain;

    public GoalManager(Brain brain) {

        this.brain = brain;
    }

    public Goal currentGoal() {

        return brain.currentGoal();
    }

    public void completeCurrentGoal() {

        brain.completeGoal();
    }

    public boolean isFinished() {

        return currentGoal() == Goal.FINISHED;
    }

    public void reset() {

        brain.setGoal(Goal.OPEN_APPLICATION);
    }

    public void printCurrentGoal() {

        System.out.println(
                "[Goal] " + currentGoal()
        );
    }
}