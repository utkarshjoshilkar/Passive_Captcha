package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.UserFeatures;
import com.passivecaptcha.server.repository.UserFeaturesRepository;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Exports the user_features table as a machine-learning-ready CSV.
 *
 * Each row represents one session with all 35+ computed features
 * plus the label (HUMAN / BOT / UNKNOWN) as the target variable.
 *
 * CSV can be directly loaded by pandas, scikit-learn, Weka, etc.
 */
@Service
public class CsvExportService {

    private final UserFeaturesRepository repo;

    public CsvExportService(UserFeaturesRepository repo) {
        this.repo = repo;
    }

    private static final String HEADER =
        "id," +
        // Mouse
        "numPointerMoves,totalPointerDistance,avgPointerSpeed,maxPointerSpeed," +
        "speedVariance,avgPointerAcceleration,maxPointerAcceleration,avgPointerJerk," +
        "pathCurvature,straightnessRatio,mouseDirectionChanges,hesitationCount," +
        "idleTimeMs,mouseEntropy," +
        // Click
        "clickCount,doubleClickCount,avgClickIntervalMs,clickPositionVariance,rightClickCount," +
        // Scroll
        "numScrolls,scrollDirectionChanges,avgScrollDistance,avgScrollSpeed," +
        "scrollSpeedVariance,scrollEntropy,scrollPauseTimeMs," +
        // Keyboard
        "usedKeyboard,keyPressCount,avgKeyIntervalMs,typingSpeed," +
        "backspaceCount,backspaceRatio,pasteEventCount,copyEventCount," +
        // Browser
        "focusChanges,visibilityChanges,windowResizeCount," +
        // Statistical
        "speedEntropy,interactionDensity,eventRatio," +
        // Meta
        "sessionDurationMs,score,decision," +
        // Target
        "label";

    /**
     * Returns the full CSV as a String.
     * Filter by label if provided (HUMAN, BOT, UNKNOWN), or null for all.
     */
    public String exportCsv(String labelFilter) {
        List<UserFeatures> sessions = repo.findAll();
        StringWriter sw = new StringWriter(sessions.size() * 200 + 512);
        PrintWriter pw  = new PrintWriter(sw);
        pw.println(HEADER);

        for (UserFeatures f : sessions) {
            if (labelFilter != null && !labelFilter.equalsIgnoreCase(f.getLabel().name())) {
                continue;
            }
            pw.println(row(f));
        }
        pw.flush();
        return sw.toString();
    }

    private String row(UserFeatures f) {
        return f.getId() + "," +
            // Mouse
            f.getNumPointerMoves() + "," +
            fmt(f.getTotalPointerDistance()) + "," +
            fmt(f.getAvgPointerSpeed()) + "," +
            fmt(f.getMaxPointerSpeed()) + "," +
            fmt(f.getSpeedVariance()) + "," +
            fmt(f.getAvgPointerAcceleration()) + "," +
            fmt(f.getMaxPointerAcceleration()) + "," +
            fmt(f.getAvgPointerJerk()) + "," +
            fmt(f.getPathCurvature()) + "," +
            fmt(f.getStraightnessRatio()) + "," +
            f.getMouseDirectionChanges() + "," +
            f.getHesitationCount() + "," +
            f.getIdleTimeMs() + "," +
            fmt(f.getMouseEntropy()) + "," +
            // Click
            f.getClickCount() + "," +
            f.getDoubleClickCount() + "," +
            fmt(f.getAvgClickIntervalMs()) + "," +
            fmt(f.getClickPositionVariance()) + "," +
            f.getRightClickCount() + "," +
            // Scroll
            f.getNumScrolls() + "," +
            f.getScrollDirectionChanges() + "," +
            fmt(f.getAvgScrollDistance()) + "," +
            fmt(f.getAvgScrollSpeed()) + "," +
            fmt(f.getScrollSpeedVariance()) + "," +
            fmt(f.getScrollEntropy()) + "," +
            fmt(f.getScrollPauseTimeMs()) + "," +
            // Keyboard
            (f.isUsedKeyboard() ? 1 : 0) + "," +
            f.getKeyPressCount() + "," +
            fmt(f.getAvgKeyIntervalMs()) + "," +
            fmt(f.getTypingSpeed()) + "," +
            f.getBackspaceCount() + "," +
            fmt(f.getBackspaceRatio()) + "," +
            f.getPasteEventCount() + "," +
            f.getCopyEventCount() + "," +
            // Browser
            f.getFocusChanges() + "," +
            f.getVisibilityChanges() + "," +
            f.getWindowResizeCount() + "," +
            // Statistical
            fmt(f.getSpeedEntropy()) + "," +
            fmt(f.getInteractionDensity()) + "," +
            fmt(f.getEventRatio()) + "," +
            // Meta
            f.getSessionDuration() + "," +
            fmt(f.getScore()) + "," +
            f.getDecision() + "," +
            // Target
            f.getLabel().name();
    }

    /** Format double to 6 decimal places */
    private String fmt(double v) {
        return String.format("%.6f", v);
    }
}
