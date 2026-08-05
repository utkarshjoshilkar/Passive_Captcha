package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.RawEvent;
import com.passivecaptcha.server.model.UserFeatures;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Computes the full behavioral feature vector from a raw event stream.
 *
 * Provides a reusable statistical toolkit and computes extended behavioral biometrics.
 *
 * All time units are milliseconds. All distance units are pixels.
 * deltaTime <= 0 intervals are skipped to avoid division by zero.
 */
@Service
public class FeatureExtractorService {

    // Hesitation threshold: speed below this for 100+ ms = hesitation
    private static final double HESITATION_SPEED_THRESHOLD = 0.1; // px/ms
    // Idle threshold: no interaction gap > 1000 ms = idle
    private static final long   IDLE_THRESHOLD_MS = 1000L;
    // Direction change threshold: angle diff > 45° = direction change
    private static final double DIR_CHANGE_ANGLE   = Math.PI / 4.0;
    // Double-click: two clicks within 300 ms
    private static final long   DOUBLE_CLICK_MS    = 300L;

    /**
     * Computes all features from the raw event stream and populates
     * a UserFeatures instance. The caller is responsible for setting
     * meta fields (ipAddress, userAgent, label, score, decision).
     */
    public UserFeatures extract(List<RawEvent> events) {
        UserFeatures f = new UserFeatures();
        if (events == null || events.isEmpty()) return f;

        // Sort by timestamp to ensure correct ordering
        List<RawEvent> sorted = events.stream()
                .sorted(Comparator.comparingLong(RawEvent::getTimestamp))
                .collect(Collectors.toList());

        long sessionStart = sorted.get(0).getTimestamp();
        long sessionEnd   = sorted.get(sorted.size() - 1).getTimestamp();
        long sessionDurationMs = Math.max(1L, sessionEnd - sessionStart);
        f.setSessionDuration(sessionDurationMs);

        // ── Partition events by type ──────────────────────────────────────────
        List<RawEvent> mouseEvents  = filter(sorted, "mousemove");
        List<RawEvent> clickEvents  = filter(sorted, "click");
        List<RawEvent> rClickEvents = filter(sorted, "rightclick");
        List<RawEvent> scrollEvents = filter(sorted, "scroll");
        List<RawEvent> keyEvents    = filter(sorted, "keydown");
        List<RawEvent> pasteEvents  = filter(sorted, "paste");
        List<RawEvent> copyEvents   = filter(sorted, "copy");
        List<RawEvent> focusEvents  = filterAny(sorted, "focus", "blur");
        List<RawEvent> visEvents    = filter(sorted, "visibilitychange");
        List<RawEvent> resizeEvents = filter(sorted, "resize");

        List<Double> mouseSpeeds = extractMouseFeatures(f, mouseEvents, sessionDurationMs);
        extractClickFeatures(f, clickEvents, rClickEvents);
        extractScrollFeatures(f, scrollEvents);
        extractKeyboardFeatures(f, keyEvents, pasteEvents, copyEvents);
        extractBrowserFeatures(f, focusEvents, visEvents, resizeEvents);
        extractStatisticalFeatures(f, sorted, sessionDurationMs,
                mouseEvents, clickEvents, scrollEvents, keyEvents, mouseSpeeds);

        return f;
    }

    // ─────────────────────────────── MOUSE ────────────────────────────────────
    private List<Double> extractMouseFeatures(UserFeatures f, List<RawEvent> events, long sessionDurationMs) {
        int n = events.size();
        f.setNumPointerMoves(n);
        List<Double> speeds = new ArrayList<>();
        if (n < 2) {
            f.setIdleRatio(sessionDurationMs > 0 ? Math.min(1.0, (double) f.getIdleTimeMs() / sessionDurationMs) : 0.0);
            return speeds;
        }

        double totalDist = 0;
        double prevSpeed = -1;
        double prevAcc   = Double.NaN;
        List<Double> accs   = new ArrayList<>();
        List<Double> jerks  = new ArrayList<>();
        List<Double> angles = new ArrayList<>();
        List<Double> curvatures = new ArrayList<>();
        List<Double> hesitationDurations = new ArrayList<>();

        int  dirChanges  = 0;
        int  hesitations = 0;
        long idleMs      = 0;

        // Start/end for straightness ratio
        double startX = events.get(0).getX() != null ? events.get(0).getX() : 0;
        double startY = events.get(0).getY() != null ? events.get(0).getY() : 0;
        double endX   = events.get(n - 1).getX() != null ? events.get(n - 1).getX() : 0;
        double endY   = events.get(n - 1).getY() != null ? events.get(n - 1).getY() : 0;

        Double lastAngle = null;
        long   hesStart  = -1;

        for (int i = 1; i < n; i++) {
            RawEvent prev = events.get(i - 1);
            RawEvent cur  = events.get(i);

            double dt = cur.getTimestamp() - prev.getTimestamp();
            if (dt <= 0) continue;

            double dx = safeX(cur) - safeX(prev);
            double dy = safeY(cur) - safeY(prev);
            double dist = Math.sqrt(dx * dx + dy * dy);
            totalDist += dist;

            double speed = dist / dt; // px/ms
            speeds.add(speed);

            // Idle detection
            if (dt > IDLE_THRESHOLD_MS) idleMs += dt;

            // Hesitation detection
            if (speed < HESITATION_SPEED_THRESHOLD) {
                if (hesStart < 0) hesStart = prev.getTimestamp();
            } else {
                if (hesStart >= 0) {
                    long duration = cur.getTimestamp() - hesStart;
                    if (duration >= 100) {
                        hesitations++;
                        hesitationDurations.add((double) duration);
                    }
                    hesStart = -1;
                }
            }

            // Angle / direction
            double angle = Math.atan2(dy, dx);
            angles.add(angle);
            if (lastAngle != null) {
                double diff = Math.abs(angle - lastAngle);
                if (diff > Math.PI) diff = 2 * Math.PI - diff;
                curvatures.add(diff);
                if (diff > DIR_CHANGE_ANGLE) dirChanges++;
            }
            lastAngle = angle;

            // Acceleration
            if (prevSpeed >= 0) {
                double acc = Math.abs(speed - prevSpeed) / dt;
                accs.add(acc);

                // Jerk
                if (!Double.isNaN(prevAcc)) {
                    double jerk = Math.abs(acc - prevAcc) / dt;
                    jerks.add(jerk);
                }
                prevAcc = acc;
            }
            prevSpeed = speed;
        }

        // Trailing hesitation check
        if (hesStart >= 0) {
            long duration = events.get(n - 1).getTimestamp() - hesStart;
            if (duration >= 100) {
                hesitations++;
                hesitationDurations.add((double) duration);
            }
        }

        // Speed features
        f.setTotalPointerDistance(totalDist);
        f.setAvgPointerSpeed(mean(speeds));
        f.setMaxPointerSpeed(maximum(speeds));
        f.setSpeedVariance(variance(speeds));
        f.setMedianPointerSpeed(median(speeds));
        f.setPointerSpeedStdDev(standardDeviation(speeds));
        f.setPointerSpeedIQR(interquartileRange(speeds));

        // Acceleration features
        f.setAvgPointerAcceleration(mean(accs));
        f.setMaxPointerAcceleration(maximum(accs));
        f.setMedianPointerAcceleration(median(accs));
        f.setPointerAccelerationStdDev(standardDeviation(accs));
        f.setPointerAccelerationVariance(variance(accs));

        // Jerk features
        f.setAvgPointerJerk(mean(jerks));
        f.setMaxPointerJerk(maximum(jerks));
        f.setPointerJerkVariance(variance(jerks));

        // Curvature features
        f.setPathCurvature(mean(curvatures));
        f.setMaximumCurvature(maximum(curvatures));
        f.setCurvatureVariance(variance(curvatures));

        // Straightness & direction changes
        double directDist = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        f.setStraightnessRatio(totalDist > 0 ? Math.min(directDist / totalDist, 1.0) : 0);
        f.setMouseDirectionChanges(dirChanges);

        // Hesitation & Idle
        f.setHesitationCount(hesitations);
        f.setAverageHesitationDuration(mean(hesitationDurations));
        f.setMaximumHesitationDuration(maximum(hesitationDurations));
        f.setIdleTimeMs(idleMs);
        f.setIdleRatio(sessionDurationMs > 0 ? Math.min(1.0, (double) idleMs / sessionDurationMs) : 0.0);

        // Mouse entropy
        f.setMouseEntropy(shannonEntropy(quantiseAngles(angles)));

        return speeds;
    }

    // ─────────────────────────────── CLICK ────────────────────────────────────
    private void extractClickFeatures(UserFeatures f, List<RawEvent> clicks,
                                      List<RawEvent> rClicks) {
        f.setClickCount(clicks.size());
        f.setRightClickCount(rClicks.size());

        if (clicks.size() < 2) return;

        int dblClicks = 0;
        List<Double> intervals = new ArrayList<>();
        List<Double> xs = new ArrayList<>(), ys = new ArrayList<>();

        for (int i = 1; i < clicks.size(); i++) {
            long gap = clicks.get(i).getTimestamp() - clicks.get(i - 1).getTimestamp();
            intervals.add((double) gap);
            if (gap <= DOUBLE_CLICK_MS) dblClicks++;
        }

        for (RawEvent e : clicks) {
            if (e.getX() != null) xs.add(e.getX());
            if (e.getY() != null) ys.add(e.getY());
        }

        f.setDoubleClickCount(dblClicks);
        f.setAvgClickIntervalMs(mean(intervals));
        f.setMedianClickInterval(median(intervals));
        f.setMaximumClickInterval(maximum(intervals));

        // Position variance = avg of X-variance and Y-variance
        double posVar = (variance(xs) + variance(ys)) / 2.0;
        f.setClickPositionVariance(posVar);
    }

    // ─────────────────────────────── SCROLL ───────────────────────────────────
    private void extractScrollFeatures(UserFeatures f, List<RawEvent> events) {
        int n = events.size();
        f.setNumScrolls(n);
        if (n < 1) return;

        List<Double> distances  = new ArrayList<>();
        List<Double> speeds     = new ArrayList<>();
        List<Double> pauses     = new ArrayList<>();
        int          dirChanges = 0;
        String       lastDir    = null;

        for (int i = 0; i < n; i++) {
            double dy = events.get(i).getDy() != null ? Math.abs(events.get(i).getDy()) : 0;
            distances.add(dy);
            String dir = (events.get(i).getDy() != null && events.get(i).getDy() >= 0) ? "down" : "up";
            if (lastDir != null && !dir.equals(lastDir)) dirChanges++;
            lastDir = dir;

            if (i > 0) {
                long dt = events.get(i).getTimestamp() - events.get(i - 1).getTimestamp();
                if (dt > 0) {
                    speeds.add(dy / dt);
                    pauses.add((double) dt);
                }
            }
        }

        f.setScrollDirectionChanges(dirChanges);
        f.setAvgScrollDistance(mean(distances));
        f.setAvgScrollSpeed(mean(speeds));
        f.setScrollSpeedVariance(variance(speeds));
        f.setScrollEntropy(shannonEntropy(quantiseValues(distances, 10)));
        f.setScrollPauseTimeMs(mean(pauses));
    }

    // ─────────────────────────────── KEYBOARD ─────────────────────────────────
    private void extractKeyboardFeatures(UserFeatures f, List<RawEvent> keys,
                                         List<RawEvent> pastes, List<RawEvent> copies) {
        int total = keys.size();
        f.setKeyPressCount(total);
        f.setUsedKeyboard(total > 0);
        f.setPasteEventCount(pastes.size());
        f.setCopyEventCount(copies.size());

        int backspaces = (int) keys.stream()
                .filter(e -> "Backspace".equals(e.getKeyName()) || "Delete".equals(e.getKeyName()))
                .count();
        f.setBackspaceCount(backspaces);
        f.setBackspaceRatio(total > 0 ? (double) backspaces / total : 0);

        if (total >= 2) {
    List<Double> intervals = new ArrayList<>();
    for (int i = 1; i < total; i++) {
        long gap = keys.get(i).getTimestamp() - keys.get(i - 1).getTimestamp();
        if (gap > 0) intervals.add((double) gap);
    }

    f.setAvgKeyIntervalMs(mean(intervals));
    f.setMedianKeyInterval(median(intervals));
    f.setKeyIntervalStdDev(standardDeviation(intervals));
    f.setKeyIntervalVariance(variance(intervals));

    // Typing speed in chars/second based on key press duration
    long firstKey = keys.get(0).getTimestamp();
    long lastKey = keys.get(total - 1).getTimestamp();

    long typingDurationMs = lastKey - firstKey;

    if (typingDurationMs >= 100) {
        double typingDurationSec = typingDurationMs / 1000.0;
        f.setTypingSpeed(total / typingDurationSec);
    } else {
        // Ignore unrealistically short typing bursts
        f.setTypingSpeed(0.0);
    }

} else {
    f.setTypingSpeed(0.0);
}
}
    // ─────────────────────────────── BROWSER ──────────────────────────────────
    private void extractBrowserFeatures(UserFeatures f, List<RawEvent> focusEvents,
                                        List<RawEvent> visEvents, List<RawEvent> resizeEvents) {
        f.setFocusChanges(focusEvents.size());
        f.setVisibilityChanges(visEvents.size());
        f.setWindowResizeCount(resizeEvents.size());
    }

    // ─────────────────────────────── STATISTICAL ──────────────────────────────
    private void extractStatisticalFeatures(UserFeatures f, List<RawEvent> all,
                                            long sessionMs,
                                            List<RawEvent> mouse, List<RawEvent> clicks,
                                            List<RawEvent> scrolls, List<RawEvent> keys,
                                            List<Double> mouseSpeeds) {
        // Speed entropy — computed from quantised speeds reused from mouse extraction
        f.setSpeedEntropy(shannonEntropy(quantiseValues(mouseSpeeds, 10)));

        // Interaction density = events per second
        double sessionSec = sessionMs / 1000.0;
        f.setInteractionDensity(sessionSec > 0 ? all.size() / sessionSec : 0.0);

        // Event ratio = mouse moves / (clicks + scrolls + keys)
        int denom = clicks.size() + scrolls.size() + keys.size();
        f.setEventRatio(denom > 0 ? (double) mouse.size() / denom : mouse.size());

        // Overall event entropy across defined target event types
        Set<String> targetTypes = new HashSet<>(Arrays.asList(
                "mousemove", "click", "rightclick", "scroll", "keydown",
                "copy", "paste", "focus", "blur", "resize", "visibilitychange"
        ));
        List<String> eventTypes = all.stream()
                .map(RawEvent::getType)
                .filter(targetTypes::contains)
                .collect(Collectors.toList());
        f.setOverallEventEntropy(shannonEntropy(eventTypes));

        // Peak events per second across one-second windows
        if (all.isEmpty()) {
            f.setPeakEventsPerSecond(0);
        } else {
            long sessionStart = all.get(0).getTimestamp();
            Map<Long, Integer> windowCounts = new HashMap<>();
            for (RawEvent e : all) {
                long windowIndex = (e.getTimestamp() - sessionStart) / 1000L;
                windowCounts.put(windowIndex, windowCounts.getOrDefault(windowIndex, 0) + 1);
            }
            int maxEvents = windowCounts.values().stream().mapToInt(Integer::intValue).max().orElse(0);
            f.setPeakEventsPerSecond(maxEvents);
        }
    }

    // ─────────────────────────────── STATISTICAL TOOLKIT LIBRARY ─────────────────────────────

    public double minimum(List<Double> values) {
        if (values == null || values.isEmpty()) return 0.0;
        return values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }

    public double maximum(List<Double> values) {
        if (values == null || values.isEmpty()) return 0.0;
        return values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    public double median(List<Double> values) {
        return percentile(values, 50.0);
    }

    public double percentile(List<Double> values, double percentile) {
        if (values == null || values.isEmpty()) return 0.0;
        if (values.size() == 1) return values.get(0);
        if (percentile <= 0) return minimum(values);
        if (percentile >= 100) return maximum(values);

        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        double rank = (percentile / 100.0) * (sorted.size() - 1);
        int lower = (int) Math.floor(rank);
        int upper = (int) Math.ceil(rank);
        double weight = rank - lower;

        return sorted.get(lower) + weight * (sorted.get(upper) - sorted.get(lower));
    }

    public double interquartileRange(List<Double> values) {
        if (values == null || values.isEmpty()) return 0.0;
        return percentile(values, 75.0) - percentile(values, 25.0);
    }

    public double standardDeviation(List<Double> values) {
        return Math.sqrt(variance(values));
    }

    public double mean(List<Double> values) {
        if (values == null || values.isEmpty()) return 0.0;
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double variance(List<Double> values) {
    if (values == null || values.size() < 2)
        return 0.0;

    double m = mean(values);

    double sumSquared = values.stream()
            .mapToDouble(v -> {
                double diff = v - m;
                return diff * diff;
            })
            .sum();

    return sumSquared / (values.size() - 1);
}

    /**
     * Shannon entropy: H = -Σ p(x) * log2(p(x))
     * Input: list of category labels (Strings)
     */
    private double shannonEntropy(List<String> categories) {
        if (categories == null || categories.isEmpty()) return 0.0;
        Map<String, Long> counts = categories.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        int total = categories.size();
        double entropy = 0;
        for (long count : counts.values()) {
            double p = (double) count / total;
            if (p > 0) entropy -= p * (Math.log(p) / Math.log(2));
        }
        return entropy;
    }

    /** Quantise continuous angles into 8 octant buckets */
    private List<String> quantiseAngles(List<Double> angles) {
        if (angles == null || angles.isEmpty()) return Collections.emptyList();
        List<String> buckets = new ArrayList<>();
        for (double a : angles) {
            int octant = (int) Math.floor(((a + Math.PI) / (2 * Math.PI)) * 8) % 8;
            buckets.add(String.valueOf(octant));
        }
        return buckets;
    }

    /** Quantise continuous values into {@code bins} equal-width buckets */
    private List<String> quantiseValues(List<Double> vals, int bins) {
        if (vals == null || vals.isEmpty()) return Collections.emptyList();
        double min = minimum(vals);
        double max = maximum(vals);
        double range = max - min;
        List<String> buckets = new ArrayList<>();
        for (double v : vals) {
            int b = range > 0 ? (int) Math.min(((v - min) / range) * bins, bins - 1) : 0;
            buckets.add(String.valueOf(b));
        }
        return buckets;
    }

    private double safeX(RawEvent e) { return e.getX() != null ? e.getX() : 0; }
    private double safeY(RawEvent e) { return e.getY() != null ? e.getY() : 0; }

    /** Filter events by exact type */
    private List<RawEvent> filter(List<RawEvent> src, String type) {
        return src.stream().filter(e -> type.equals(e.getType())).collect(Collectors.toList());
    }

    /** Filter events matching any of the given types */
    private List<RawEvent> filterAny(List<RawEvent> src, String... types) {
        Set<String> set = new HashSet<>(Arrays.asList(types));
        return src.stream().filter(e -> set.contains(e.getType())).collect(Collectors.toList());
    }
}
