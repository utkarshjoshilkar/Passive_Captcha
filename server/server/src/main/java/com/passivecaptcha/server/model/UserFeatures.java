package com.passivecaptcha.server.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Stores the full behavioral feature vector for a single user session.
 * Hibernate ddl-auto=update will automatically add new columns to the existing table.
 *
 * Feature groups:
 *  - Mouse (11 features)
 *  - Click (4 features)
 *  - Scroll (5 features, 3 existing)
 *  - Keyboard (6 features, 1 existing)
 *  - Browser (5 features)
 *  - Statistical (4 features)
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
    /** Average |acceleration| between consecutive speed samples */
    private double avgPointerAcceleration;
    /** Maximum |acceleration| observed */
    private double maxPointerAcceleration;
    /** Average |jerk| = rate of change of acceleration */
    private double avgPointerJerk;
    /** Average turning angle between consecutive movement vectors (radians) */
    private double pathCurvature;
    /** directDistance / actualPathDistance — close to 1 = bot-like straight line */
    private double straightnessRatio;
    /** Count of times movement direction changes by > 45° */
    private int    mouseDirectionChanges;
    /** Count of hesitation events (speed < threshold for 100+ ms) */
    private int    hesitationCount;
    /** Accumulated milliseconds with no user interaction (> 1000 ms gaps) */
    private long   idleTimeMs;
    /** Shannon entropy of movement direction angles */
    private double mouseEntropy;

    // ─────────────────────────────── CLICK FEATURES ───────────────────────────
    /** Total left-click count */
    private int    clickCount;
    /** Clicks within 300 ms of each other */
    private int    doubleClickCount;
    /** Average milliseconds between consecutive clicks */
    private double avgClickIntervalMs;
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
    /** Characters per second (keyPressCount / sessionDuration) */
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

    public double getAvgPointerAcceleration()      { return avgPointerAcceleration; }
    public void   setAvgPointerAcceleration(double v){ this.avgPointerAcceleration = v; }

    public double getMaxPointerAcceleration()      { return maxPointerAcceleration; }
    public void   setMaxPointerAcceleration(double v){ this.maxPointerAcceleration = v; }

    public double getAvgPointerJerk()              { return avgPointerJerk; }
    public void   setAvgPointerJerk(double v)      { this.avgPointerJerk = v; }

    public double getPathCurvature()               { return pathCurvature; }
    public void   setPathCurvature(double v)       { this.pathCurvature = v; }

    public double getStraightnessRatio()           { return straightnessRatio; }
    public void   setStraightnessRatio(double v)   { this.straightnessRatio = v; }

    public int    getMouseDirectionChanges()        { return mouseDirectionChanges; }
    public void   setMouseDirectionChanges(int v)   { this.mouseDirectionChanges = v; }

    public int    getHesitationCount()             { return hesitationCount; }
    public void   setHesitationCount(int v)        { this.hesitationCount = v; }

    public long   getIdleTimeMs()                  { return idleTimeMs; }
    public void   setIdleTimeMs(long v)            { this.idleTimeMs = v; }

    public double getMouseEntropy()                { return mouseEntropy; }
    public void   setMouseEntropy(double v)        { this.mouseEntropy = v; }

    // Click
    public int    getClickCount()                  { return clickCount; }
    public void   setClickCount(int v)             { this.clickCount = v; }

    public int    getDoubleClickCount()            { return doubleClickCount; }
    public void   setDoubleClickCount(int v)       { this.doubleClickCount = v; }

    public double getAvgClickIntervalMs()          { return avgClickIntervalMs; }
    public void   setAvgClickIntervalMs(double v)  { this.avgClickIntervalMs = v; }

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
}