package com.passivecaptcha.bot.botd.util.brain;

public class Brain {

    private Goal currentGoal;

    private boolean thinking;

    public Brain() {

        this.currentGoal = Goal.OPEN_APPLICATION;
        this.thinking = false;
    }

    public Goal getCurrentGoal() {
        return currentGoal;
    }

    public void setCurrentGoal(Goal currentGoal) {
        this.currentGoal = currentGoal;
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