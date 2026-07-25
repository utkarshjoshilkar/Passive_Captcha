package com.passivecaptcha.server.model;

import jakarta.persistence.*;

/**
 * Stores a single raw browser event from a user session.
 * The raw event stream allows server-side feature recomputation
 * if scoring algorithms improve — a research-oriented design.
 *
 * Linked to UserFeatures via sessionId (the UserFeatures PK).
 */
@Entity
@Table(name = "session_raw_events", indexes = {
    @Index(name = "idx_raw_session", columnList = "session_id")
})
public class RawEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** FK → user_features.id — groups all events for one session */
    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    /**
     * Event type: mousemove | click | rightclick | scroll |
     *             keydown | paste | copy | focus | blur |
     *             visibilitychange | resize
     */
    @Column(nullable = false, length = 30)
    private String type;

    /** Mouse / click X coordinate (null for non-pointer events) */
    private Double x;

    /** Mouse / click Y coordinate (null for non-pointer events) */
    private Double y;

    /** Scroll delta Y in pixels (null for non-scroll events) */
    private Double dy;

    /** Key identifier e.g. "a", "Backspace" (null for non-keyboard events) */
    @Column(name = "key_name", length = 30)
    private String keyName;

    /** Browser-reported epoch millisecond timestamp of the event */
    @Column(nullable = false)
    private long timestamp;

    // ─────────────────────────────── CONSTRUCTORS ─────────────────────────────
    public RawEvent() {}

    public RawEvent(Long sessionId, String type, Double x, Double y,
                    Double dy, String keyName, long timestamp) {
        this.sessionId = sessionId;
        this.type      = type;
        this.x         = x;
        this.y         = y;
        this.dy        = dy;
        this.keyName   = keyName;
        this.timestamp = timestamp;
    }

    // ─────────────────────────────── GETTERS / SETTERS ────────────────────────
    public Long   getId()                     { return id; }
    public void   setId(Long id)              { this.id = id; }

    public Long   getSessionId()              { return sessionId; }
    public void   setSessionId(Long v)        { this.sessionId = v; }

    public String getType()                   { return type; }
    public void   setType(String v)           { this.type = v; }

    public Double getX()                      { return x; }
    public void   setX(Double v)              { this.x = v; }

    public Double getY()                      { return y; }
    public void   setY(Double v)              { this.y = v; }

    public Double getDy()                     { return dy; }
    public void   setDy(Double v)             { this.dy = v; }

    public String getKeyName()                { return keyName; }
    public void   setKeyName(String v)        { this.keyName = v; }

    public long   getTimestamp()              { return timestamp; }
    public void   setTimestamp(long v)        { this.timestamp = v; }
}
