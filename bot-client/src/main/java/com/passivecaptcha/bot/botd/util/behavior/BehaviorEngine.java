package com.passivecaptcha.bot.botd.util.behavior;

import com.passivecaptcha.bot.botd.util.brain.Brain;
import com.passivecaptcha.bot.botd.util.brain.BrainCycle;
import com.passivecaptcha.bot.botd.util.brain.CognitiveState;
import com.passivecaptcha.bot.botd.util.brain.Memory;

import java.util.List;

public class BehaviorEngine {

    private final Brain brain;
    private final Memory memory;
    private final BrainCycle brainCycle;

    public BehaviorEngine(
            Brain brain,
            Memory memory,
            BrainCycle brainCycle
    ) {

        this.brain = brain;
        this.memory = memory;
        this.brainCycle = brainCycle;
    }

    public void think() throws InterruptedException {

        brain.startThinking();

        List<CognitiveState> cycle =
                brainCycle.nextCycle();

        for (CognitiveState state : cycle) {

            switch (state) {

                case LOOK -> look();

                case READ -> read();

                case THINK -> thinkPause();

                case VERIFY -> verify();

                case ACT -> {
                }
            }
        }

        brain.stopThinking();
    }

    private void look() throws InterruptedException {

        Thread.sleep(120);
    }

    private void read() throws InterruptedException {

        Thread.sleep(220);
    }

    private void thinkPause() throws InterruptedException {

        Thread.sleep(350);
    }

    private void verify() throws InterruptedException {

        Thread.sleep(180);
    }

}