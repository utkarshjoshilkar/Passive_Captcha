package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.UserFeatures;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    // Weights for different features
    private static final double POINTER_MOVE_WEIGHT = 0.3;
    private static final double SPEED_WEIGHT = 0.3;
    private static final double KEYBOARD_WEIGHT = 0.2;
    private static final double SESSION_WEIGHT = 0.2;

    public double calculateScore(UserFeatures features) {
        double score = 0.0;

        // Pointer moves: More moves = more human-like (0-50 moves considered normal)
        double moveScore = Math.min(features.getNumPointerMoves() / 50.0, 1.0);
        score += moveScore * POINTER_MOVE_WEIGHT;

        // Speed: Moderate speed = human-like (0.5-2.0 pixels/ms)
        double speed = features.getAvgPointerSpeed();
        double speedScore = 0.0;
        if (speed >= 0.5 && speed <= 2.0) {
            speedScore = 1.0; // Perfect human speed
        } else if (speed > 2.0) {
            speedScore = Math.max(0, 1.0 - (speed - 2.0) / 5.0); // Too fast = suspicious
        } else {
            speedScore = speed / 0.5; // Too slow = suspicious
        }
        score += speedScore * SPEED_WEIGHT;

        // Keyboard usage: Using keyboard = more human-like
        if (features.isUsedKeyboard()) {
            score += KEYBOARD_WEIGHT;
        }

        // Session duration: Longer session = more human-like (> 2 seconds)
        double sessionScore = Math.min(features.getSessionDuration() / 10000.0, 1.0); // 10 seconds max
        score += sessionScore * SESSION_WEIGHT;

        return Math.min(Math.max(score, 0.0), 1.0); // Ensure between 0 and 1
    }
}