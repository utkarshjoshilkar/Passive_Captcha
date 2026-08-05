package com.passivecaptcha.bot.bote.util.brain;

public class Brain {

    private Goal currentGoal;

    private boolean thinking;

    public Brain() {

        currentGoal = Goal.OPEN_APPLICATION;

        thinking = false;
    }

    public Goal currentGoal() {

        return currentGoal;
    }

    public void setGoal(Goal goal) {

        currentGoal = goal;
    }

    public void completeGoal() {

        currentGoal = switch (currentGoal) {

            case OPEN_APPLICATION ->
                    Goal.OPEN_DEMO;

            case OPEN_DEMO ->
                    Goal.ENTER_NAME;

            case ENTER_NAME ->
                    Goal.SELECT_RATING;

            case SELECT_RATING ->
                    Goal.ENTER_FEEDBACK;

            case ENTER_FEEDBACK ->
                    Goal.SUBMIT_FEEDBACK;

            case SUBMIT_FEEDBACK ->
                    Goal.ANALYZE;

            case ANALYZE ->
                    Goal.FINISHED;

            case FINISHED ->
                    Goal.FINISHED;
        };
    }

    public boolean isThinking() {

        return thinking;
    }

    public void startThinking() {

        thinking = true;
    }

    public void stopThinking() {

        thinking = false;
    }
}