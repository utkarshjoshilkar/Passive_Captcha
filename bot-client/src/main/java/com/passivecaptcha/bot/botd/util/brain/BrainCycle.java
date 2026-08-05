package com.passivecaptcha.bot.botd.util.brain;

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

        if (memory.getAttention() > 70) {
            cycle.add(CognitiveState.READ);
        }

        if (ThreadLocalRandom.current().nextInt(100) < 40) {
            cycle.add(CognitiveState.THINK);
        }

        if (memory.getConfidence() < 60) {
            cycle.add(CognitiveState.VERIFY);
        }

        cycle.add(CognitiveState.ACT);

        return cycle;
    }
}