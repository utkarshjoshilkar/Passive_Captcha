package com.passivecaptcha.server.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Stores the full behavioral feature vector for a single user session.
 * Hibernate ddl-auto=update will automatically add new columns to the existing table.
 *
 * Feature groups:
 *  - Mouse (24 features)
 *  - Click (6 features)
 *  - Scroll (5 features)
 *  - Keyboard (8 features)
 *  - Browser (3 features)
 *  - Statistical (5 features)
 *  - Meta (label, score, decision, ip, userAgent)
 */
@Entity
@Table(name = "user_features")
public class UserFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─────────────────────────────── MOUSE FEATURES ───────────────────────────
    /** Number of mousemove events recorded */
    private int    numPointerMoves;
    /** Total Euclidean path length in pixels */
    private double totalPointerDistance;
    /** Average speed = totalDistance / totalTime */
    private double avgPointerSpeed;
    /** Maximum instantaneous speed across all intervals */
    private double maxPointerSpeed;
    /** Variance of instantaneous speeds — high variance = human */
    private double speedVariance;

    /**
     * Median Pointer Speed
     * <p><b>Formula:</b> median(instantaneous_speeds)</p>
     * <p><b>Unit:</b> px/ms</p>
     * <p><b>Behavioral significance:</b> Central tendency of pointer velocity that is robust against outliers and rapid erratic movements.</p>
     */
    private double medianPointerSpeed;

    /**
     * Pointer Speed Standard Deviation
     * <p><b>Formula:</b> sqrt(variance(instantaneous_speeds))</p>
     * <p><b>Unit:</b> px/ms</p>
     * <p><b>Behavioral significance:</b> Indicates speed variability. Human movements feature smooth acceleration curves with natural speed fluctuations.</p>
     */
    private double pointerSpeedStdDev;

    /**
     * Pointer Speed Interquartile Range
     * <p><b>Formula:</b> Q3(speeds) - Q1(speeds)</p>
     * <p><b>Unit:</b> px/ms</p>
     * <p><b>Behavioral significance:</b> Non-parametric measure of statistical dispersion in pointer speed, capturing natural human velocity spread.</p>
     */
    private double pointerSpeedIQR;

    /** Average |acceleration| between consecutive speed samples */
    private double avgPointerAcceleration;
    /** Maximum |acceleration| observed */
    private double maxPointerAcceleration;

    /**
     * Median Pointer Acceleration
     * <p><b>Formula:</b> median(instantaneous_accelerations)</p>
     * <p><b>Unit:</b> px/ms²</p>
     * <p><b>Behavioral significance:</b> Median magnitude of rate of change of speed, capturing baseline motor control characteristics.</p>
     */
    private double medianPointerAcceleration;

    /**
     * Pointer Acceleration Standard Deviation
     * <p><b>Formula:</b> sqrt(variance(instantaneous_accelerations))</p>
     * <p><b>Unit:</b> px/ms²</p>
     * <p><b>Behavioral significance:</b> Variation in pointer acceleration. High variation indicates human physiological neuromuscular adjustments.</p>
     */
    private double pointerAccelerationStdDev;

    /**
     * Pointer Acceleration Variance
     * <p><b>Formula:</b> variance(instantaneous_accelerations)</p>
     * <p><b>Unit:</b> (px/ms²)²</p>
     * <p><b>Behavioral significance:</b> Variance of pointer acceleration across movement intervals.</p>
     */
    private double pointerAccelerationVariance;

    /** Average |jerk| = rate of change of acceleration */
    private double avgPointerJerk;

    /**
     * Maximum Pointer Jerk
     * <p><b>Formula:</b> max(instantaneous_jerks)</p>
     * <p><b>Unit:</b> px/ms³</p>
     * <p><b>Behavioral significance:</b> Peak rate of change of acceleration. Abrupt changes in acceleration occur during sudden human direction corrections or bot artifacts.</p>
     */
    private double maxPointerJerk;

    /**
     * Pointer Jerk Variance
     * <p><b>Formula:</b> variance(instantaneous_jerks)</p>
     * <p><b>Unit:</b> (px/ms³)²</p>
     * <p><b>Behavioral significance:</b> Variance in jerk values reflecting muscle tremor and biomechanical movement smoothness.</p>
     */
    private double pointerJerkVariance;

    /** Average turning angle between consecutive movement vectors (radians) */
    private double pathCurvature;

    /**
     * Maximum Curvature
     * <p><b>Formula:</b> max(turning_angle_diffs)</p>
     * <p><b>Unit:</b> radians</p>
     * <p><b>Behavioral significance:</b> Maximum turning angle between trajectory vectors, highlighting sharp turns and trajectory changes.</p>
     */
    private double maximumCurvature;

    /**
     * Curvature Variance
     * <p><b>Formula:</b> variance(turning_angle_diffs)</p>
     * <p><b>Unit:</b> radians²</p>
     * <p><b>Behavioral significance:</b> Dispersion of trajectory direction changes along the movement path.</p>
     */
    private double curvatureVariance;

    /** directDistance / actualPathDistance — close to 1 = bot-like straight line */
    private double straightnessRatio;
    /** Count of times movement direction changes by > 45° */
    private int    mouseDirectionChanges;
    /** Count of hesitation events (speed < threshold for 100+ ms) */
    private int    hesitationCount;

    /**
     * Average Hesitation Duration
     * <p><b>Formula:</b> mean(hesitation_durations_ms)</p>
     * <p><b>Unit:</b> ms</p>
     * <p><b>Behavioral significance:</b> Mean length of micro-pauses (speed < 0.1 px/ms lasting >= 100 ms) representing decision-making pauses.</p>
     */
    private double averageHesitationDuration;

    /**
     * Maximum Hesitation Duration
     * <p><b>Formula:</b> max(hesitation_durations_ms)</p>
     * <p><b>Unit:</b> ms</p>
     * <p><b>Behavioral significance:</b> Longest micro-pause duration recorded during pointer movement.</p>
     */
    private double maximumHesitationDuration;

    /** Accumulated milliseconds with no user interaction (> 1000 ms gaps) */
    private long   idleTimeMs;

    /**
     * Idle Ratio
     * <p><b>Formula:</b> idleTimeMs / sessionDuration</p>
     * <p><b>Unit:</b> ratio (0 to 1)</p>
     * <p><b>Behavioral significance:</b> Proportion of session duration spent in inactive state (>1000 ms gaps). High idle ratios are common in human cognitive pauses.</p>
     */
    private double idleRatio;

    /** Shannon entropy of movement direction angles */
    private double mouseEntropy;

    // ─────────────────────────────── CLICK FEATURES ───────────────────────────
    /** Total left-click count */
    private int    clickCount;
    /** Clicks within 300 ms of each other */
    private int    doubleClickCount;
    /** Average milliseconds between consecutive clicks */
    private double avgClickIntervalMs;

    /**
     * Median Click Interval
     * <p><b>Formula:</b> median(inter_click_intervals_ms)</p>
     * <p><b>Unit:</b> ms</p>
     * <p><b>Behavioral significance:</b> Median time elapsed between consecutive click events.</p>
     */
    private double medianClickInterval;

    /**
     * Maximum Click Interval
     * <p><b>Formula:</b> max(inter_click_intervals_ms)</p>
     * <p><b>Unit:</b> ms</p>
     * <p><b>Behavioral significance:</b> Maximum time elapsed between consecutive click events in a session.</p>
     */
    private double maximumClickInterval;

    /** Variance of click X/Y positions */
    private double clickPositionVariance;
    /** Right-click (context menu) count */
    private int    rightClickCount;

    // ─────────────────────────────── SCROLL FEATURES ──────────────────────────
    /** Number of scroll events */
    private int    numScrolls;
    /** Count of direction reversals (down→up or up→down) */
    private int    scrollDirectionChanges;
    /** Average pixels per scroll event */
    private double avgScrollDistance;
    /** Average scroll speed in px/ms */
    private double avgScrollSpeed;
    /** Variance of scroll speeds */
    private double scrollSpeedVariance;
    /** Shannon entropy of scroll distances — low = bot-like fixed steps */
    private double scrollEntropy;
    /** Average pause duration (ms) between scroll events */
    private double scrollPauseTimeMs;

    // ─────────────────────────────── KEYBOARD FEATURES ────────────────────────
    /** Whether any key was pressed */
    private boolean usedKeyboard;
    /** Total key presses */
    private int     keyPressCount;
    /** Average milliseconds between key presses */
    private double  avgKeyIntervalMs;

    /**
     * Median Key Interval
     * <p><b>Formula:</b> median(inter_key_intervals_ms)</p>
     * <p><b>Unit:</b> ms</p>
     * <p><b>Behavioral significance:</b> Typical delay between consecutive keystrokes, insensitive to isolated long pauses.</p>
     */
    private double medianKeyInterval;

    /**
     * Key Interval Standard Deviation
     * <p><b>Formula:</b> sqrt(variance(inter_key_intervals_ms))</p>
     * <p><b>Unit:</b> ms</p>
     * <p><b>Behavioral significance:</b> Rhythmicity and motor skill variation in human typing compared to constant-delay automated keystroke injection.</p>
     */
    private double keyIntervalStdDev;

    private double keyIntervalVariance;


    /** Characters per second (keyPressCount / typingDurationSeconds) */
    private double  typingSpeed;
    /** How many backspace/delete presses */
    private int     backspaceCount;
    /** backspaceCount / keyPressCount */
    private double  backspaceRatio;
    /** Ctrl+V or paste event count */
    private int     pasteEventCount;
    /** Ctrl+C or copy event count */
    private int     copyEventCount;

    // ─────────────────────────────── BROWSER FEATURES ─────────────────────────
    /** Tab/window focus → blur → focus transitions */
    private int    focusChanges;
    /** document.visibilitychange event count */
    private int    visibilityChanges;
    /** window resize event count */
    private int    windowResizeCount;

    // ────────────────────────────── STATISTICAL FEATURES ──────────────────────
    /** Shannon entropy of pointer speeds */
    private double speedEntropy;
    /** (totalEvents) / sessionDurationSeconds */
    private double interactionDensity;
    /** mouseMoves / (clicks + scrolls + keyPresses) */
    private double eventRatio;

    /**
     * Overall Event Entropy
     * <p><b>Formula:</b> -sum(p(e) * log2(p(e))) across event types</p>
     * <p><b>Unit:</b> bits</p>
     * <p><b>Behavioral significance:</b> Diversity of interaction types (mousemove, click, scroll, keydown, etc.). Higher entropy signifies rich human multi-modal interaction.</p>
     */
    private double overallEventEntropy;

    /**
     * Peak Events Per Second
     * <p><b>Formula:</b> max(events_in_1s_window)</p>
     * <p><b>Unit:</b> events/sec</p>
     * <p><b>Behavioral significance:</b> Peak event burst rate within any 1-second window during the session, useful for identifying automated burst scripts.</p>
     */
    private int peakEventsPerSecond;
  
    // ─────────────────────────────── SESSION META ─────────────────────────────
    /** Total session duration in milliseconds */
    private long   sessionDuration;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private String ipAddress;
    private String userAgent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DetectionLabel label = DetectionLabel.UNKNOWN;

    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    private String decision;

    @Column(name = "bot_family")
private String botFamily;

@Column(name = "bot_version")
private String botVersion;

    // ─────────────────────────────── CONSTRUCTORS ─────────────────────────────
    public UserFeatures() {}

    // ─────────────────────────────── GETTERS / SETTERS ────────────────────────
    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }

    // Mouse
    public int    getNumPointerMoves()             { return numPointerMoves; }
    public void   setNumPointerMoves(int v)        { this.numPointerMoves = v; }

    public double getTotalPointerDistance()        { return totalPointerDistance; }
    public void   setTotalPointerDistance(double v){ this.totalPointerDistance = v; }

    public double getAvgPointerSpeed()             { return avgPointerSpeed; }
    public void   setAvgPointerSpeed(double v)     { this.avgPointerSpeed = v; }

    public double getMaxPointerSpeed()             { return maxPointerSpeed; }
    public void   setMaxPointerSpeed(double v)     { this.maxPointerSpeed = v; }

    public double getSpeedVariance()               { return speedVariance; }
    public void   setSpeedVariance(double v)       { this.speedVariance = v; }

    public double getMedianPointerSpeed()          { return medianPointerSpeed; }
    public void   setMedianPointerSpeed(double v)  { this.medianPointerSpeed = v; }

    public double getPointerSpeedStdDev()          { return pointerSpeedStdDev; }
    public void   setPointerSpeedStdDev(double v)  { this.pointerSpeedStdDev = v; }

    public double getPointerSpeedIQR()             { return pointerSpeedIQR; }
    public void   setPointerSpeedIQR(double v)     { this.pointerSpeedIQR = v; }

    public double getAvgPointerAcceleration()      { return avgPointerAcceleration; }
    public void   setAvgPointerAcceleration(double v){ this.avgPointerAcceleration = v; }

    public double getMaxPointerAcceleration()      { return maxPointerAcceleration; }
    public void   setMaxPointerAcceleration(double v){ this.maxPointerAcceleration = v; }

    public double getMedianPointerAcceleration()   { return medianPointerAcceleration; }
    public void   setMedianPointerAcceleration(double v){ this.medianPointerAcceleration = v; }

    public double getPointerAccelerationStdDev()   { return pointerAccelerationStdDev; }
    public void   setPointerAccelerationStdDev(double v){ this.pointerAccelerationStdDev = v; }

    public double getPointerAccelerationVariance() { return pointerAccelerationVariance; }
    public void   setPointerAccelerationVariance(double v){ this.pointerAccelerationVariance = v; }

    public double getAvgPointerJerk()              { return avgPointerJerk; }
    public void   setAvgPointerJerk(double v)      { this.avgPointerJerk = v; }

    public double getMaxPointerJerk()              { return maxPointerJerk; }
    public void   setMaxPointerJerk(double v)      { this.maxPointerJerk = v; }

    public double getPointerJerkVariance()         { return pointerJerkVariance; }
    public void   setPointerJerkVariance(double v) { this.pointerJerkVariance = v; }

    public double getPathCurvature()               { return pathCurvature; }
    public void   setPathCurvature(double v)       { this.pathCurvature = v; }

    public double getMaximumCurvature()            { return maximumCurvature; }
    public void   setMaximumCurvature(double v)    { this.maximumCurvature = v; }

    public double getCurvatureVariance()           { return curvatureVariance; }
    public void   setCurvatureVariance(double v)   { this.curvatureVariance = v; }

    public double getStraightnessRatio()           { return straightnessRatio; }
    public void   setStraightnessRatio(double v)   { this.straightnessRatio = v; }

    public int    getMouseDirectionChanges()        { return mouseDirectionChanges; }
    public void   setMouseDirectionChanges(int v)   { this.mouseDirectionChanges = v; }

    public int    getHesitationCount()             { return hesitationCount; }
    public void   setHesitationCount(int v)        { this.hesitationCount = v; }

    public double getAverageHesitationDuration()   { return averageHesitationDuration; }
    public void   setAverageHesitationDuration(double v){ this.averageHesitationDuration = v; }

    public double getMaximumHesitationDuration()   { return maximumHesitationDuration; }
    public void   setMaximumHesitationDuration(double v){ this.maximumHesitationDuration = v; }

    public long   getIdleTimeMs()                  { return idleTimeMs; }
    public void   setIdleTimeMs(long v)            { this.idleTimeMs = v; }

    public double getIdleRatio()                   { return idleRatio; }
    public void   setIdleRatio(double v)            { this.idleRatio = v; }

    public double getMouseEntropy()                { return mouseEntropy; }
    public void   setMouseEntropy(double v)        { this.mouseEntropy = v; }

    // Click
    public int    getClickCount()                  { return clickCount; }
    public void   setClickCount(int v)             { this.clickCount = v; }

    public int    getDoubleClickCount()            { return doubleClickCount; }
    public void   setDoubleClickCount(int v)       { this.doubleClickCount = v; }

    public double getAvgClickIntervalMs()          { return avgClickIntervalMs; }
    public void   setAvgClickIntervalMs(double v)  { this.avgClickIntervalMs = v; }

    public double getMedianClickInterval()         { return medianClickInterval; }
    public void   setMedianClickInterval(double v) { this.medianClickInterval = v; }

    public double getMaximumClickInterval()        { return maximumClickInterval; }
    public void   setMaximumClickInterval(double v){ this.maximumClickInterval = v; }

    public double getClickPositionVariance()       { return clickPositionVariance; }
    public void   setClickPositionVariance(double v){ this.clickPositionVariance = v; }

    public int    getRightClickCount()             { return rightClickCount; }
    public void   setRightClickCount(int v)        { this.rightClickCount = v; }

    // Scroll
    public int    getNumScrolls()                  { return numScrolls; }
    public void   setNumScrolls(int v)             { this.numScrolls = v; }

    public int    getScrollDirectionChanges()       { return scrollDirectionChanges; }
    public void   setScrollDirectionChanges(int v)  { this.scrollDirectionChanges = v; }

    public double getAvgScrollDistance()           { return avgScrollDistance; }
    public void   setAvgScrollDistance(double v)   { this.avgScrollDistance = v; }

    public double getAvgScrollSpeed()              { return avgScrollSpeed; }
    public void   setAvgScrollSpeed(double v)      { this.avgScrollSpeed = v; }

    public double getScrollSpeedVariance()         { return scrollSpeedVariance; }
    public void   setScrollSpeedVariance(double v) { this.scrollSpeedVariance = v; }

    public double getScrollEntropy()               { return scrollEntropy; }
    public void   setScrollEntropy(double v)       { this.scrollEntropy = v; }

    public double getScrollPauseTimeMs()           { return scrollPauseTimeMs; }
    public void   setScrollPauseTimeMs(double v)   { this.scrollPauseTimeMs = v; }

    // Keyboard
    public boolean isUsedKeyboard()               { return usedKeyboard; }
    public void    setUsedKeyboard(boolean v)     { this.usedKeyboard = v; }

    public int    getKeyPressCount()               { return keyPressCount; }
    public void   setKeyPressCount(int v)          { this.keyPressCount = v; }

    public double getAvgKeyIntervalMs()            { return avgKeyIntervalMs; }
    public void   setAvgKeyIntervalMs(double v)    { this.avgKeyIntervalMs = v; }

    public double getMedianKeyInterval()           { return medianKeyInterval; }
    public void   setMedianKeyInterval(double v)   { this.medianKeyInterval = v; }

    public double getKeyIntervalStdDev()           { return keyIntervalStdDev; }
    public void   setKeyIntervalStdDev(double v)   { this.keyIntervalStdDev = v; }

    public double getKeyIntervalVariance()         {return keyIntervalVariance;}
    public void   setKeyIntervalVariance(double v) {this.keyIntervalVariance = v;}

    public double getTypingSpeed()                 { return typingSpeed; }
    public void   setTypingSpeed(double v)         { this.typingSpeed = v; }

    public int    getBackspaceCount()              { return backspaceCount; }
    public void   setBackspaceCount(int v)         { this.backspaceCount = v; }

    public double getBackspaceRatio()              { return backspaceRatio; }
    public void   setBackspaceRatio(double v)      { this.backspaceRatio = v; }

    public int    getPasteEventCount()             { return pasteEventCount; }
    public void   setPasteEventCount(int v)        { this.pasteEventCount = v; }

    public int    getCopyEventCount()              { return copyEventCount; }
    public void   setCopyEventCount(int v)         { this.copyEventCount = v; }

    // Browser
    public int    getFocusChanges()                { return focusChanges; }
    public void   setFocusChanges(int v)           { this.focusChanges = v; }

    public int    getVisibilityChanges()           { return visibilityChanges; }
    public void   setVisibilityChanges(int v)      { this.visibilityChanges = v; }

    public int    getWindowResizeCount()           { return windowResizeCount; }
    public void   setWindowResizeCount(int v)      { this.windowResizeCount = v; }

    // Statistical
    public double getSpeedEntropy()                { return speedEntropy; }
    public void   setSpeedEntropy(double v)        { this.speedEntropy = v; }

    public double getInteractionDensity()          { return interactionDensity; }
    public void   setInteractionDensity(double v)  { this.interactionDensity = v; }

    public double getEventRatio()                  { return eventRatio; }
    public void   setEventRatio(double v)          { this.eventRatio = v; }

    public double getOverallEventEntropy()         { return overallEventEntropy; }
    public void   setOverallEventEntropy(double v) { this.overallEventEntropy = v; }

    public int    getPeakEventsPerSecond()         { return peakEventsPerSecond; }
    public void   setPeakEventsPerSecond(int v)    { this.peakEventsPerSecond = v; }

    // Session Meta
    public long   getSessionDuration()             { return sessionDuration; }
    public void   setSessionDuration(long v)       { this.sessionDuration = v; }

    public LocalDateTime getCreatedAt()            { return createdAt; }
    public void setCreatedAt(LocalDateTime v)      { this.createdAt = v; }

    public String getIpAddress()                   { return ipAddress; }
    public void   setIpAddress(String v)           { this.ipAddress = v; }

    public String getUserAgent()                   { return userAgent; }
    public void   setUserAgent(String v)           { this.userAgent = v; }

    public DetectionLabel getLabel()               { return label; }
    public void   setLabel(DetectionLabel v)       { this.label = v; }

    public double getScore()                       { return score; }
    public void   setScore(double v)               { this.score = v; }

    public String getDecision()                    { return decision; }
    public void   setDecision(String v)            { this.decision = v; }

    public String getBotFamily()                   {return botFamily;}
    public void   setBotFamily(String botFamily)   {this.botFamily = botFamily;}

    public String getBotVersion()                  {return botVersion;}
    public void   setBotVersion(String botVersion) {this.botVersion = botVersion;}
}