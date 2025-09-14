package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.UserFeatures;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    // Weights (can be tuned later or replaced by ML model)
    private static final double POINTER_MOVE_WEIGHT = 0.4;
    private static final double SPEED_WEIGHT = 0.3;
    private static final double KEYBOARD_WEIGHT = 0.2;
    private static final double SESSION_WEIGHT = 0.1;

    public double calculateScore(UserFeatures features) {
        double score = 0.0;

        if (features.getNumPointerMoves() > 10) score += POINTER_MOVE_WEIGHT;
        if (features.getAvgPointerSpeed() > 0.5) score += SPEED_WEIGHT;
        if (features.isUsedKeyboard()) score += KEYBOARD_WEIGHT;
        if (features.getSessionDuration() > 5000) score += SESSION_WEIGHT;

        return Math.min(score, 1.0); // keep between 0 and 1
    }
}
