package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.UserFeatures;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoreServiceTest {

    private final ScoreService scoreService = new ScoreService();

    @Test
    void humanLikeBehaviorProducesHighScoreAndAllowDecision() {
        UserFeatures features = new UserFeatures();
        features.setNumPointerMoves(25);
        features.setAvgPointerSpeed(1.2);
        features.setPathCurvature(1.0);
        features.setUsedKeyboard(true);
        features.setSessionDuration(8000L);
        features.setNumScrolls(6);
        features.setScrollDirectionChanges(2);

        Map<String, Object> result = scoreService.evaluate(features);

        assertTrue((double) result.get("score") >= 0.6);
        assertEquals("allow", result.get("decision"));
        assertEquals("low", result.get("risk"));
    }

    @Test
    void botLikeBehaviorProducesLowScoreAndChallengeDecision() {
        UserFeatures features = new UserFeatures();
        features.setNumPointerMoves(1);
        features.setAvgPointerSpeed(0.05);
        features.setPathCurvature(0.05);
        features.setUsedKeyboard(false);
        features.setSessionDuration(500L);
        features.setNumScrolls(0);
        features.setScrollDirectionChanges(0);

        Map<String, Object> result = scoreService.evaluate(features);

        assertTrue((double) result.get("score") <= 0.3);
        assertEquals("challenge", result.get("decision"));
        assertEquals("high", result.get("risk"));
    }
}
