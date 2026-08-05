package com.passivecaptcha.bot.bote.util.brain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BrainCycle {

    private final Memory memory;

    public BrainCycle(Memory memory) {

        this.memory = memory;
    }

    public List<CognitiveState> nextCycle() {

        List<CognitiveState> cycle = new ArrayList<>();

        cycle.add(CognitiveState.LOOK);

        if (memory.getAttention() > 55) {
            cycle.add(CognitiveState.READ);
        }

        if (memory.getConfidence() < 75 &&
                chance(40)) {

            cycle.add(CognitiveState.VERIFY);
        }

        if (memory.getFatigue() > 40 &&
                chance(50)) {

            cycle.add(CognitiveState.THINK);
        }

        if (memory.getPatience() < 45 &&
                chance(30)) {

            cycle.add(CognitiveState.PAUSE);
        }

        cycle.add(CognitiveState.ACT);

        return cycle;
    }

    private boolean chance(int percent) {

        return ThreadLocalRandom.current()
                .nextInt(100) < percent;
    }

}