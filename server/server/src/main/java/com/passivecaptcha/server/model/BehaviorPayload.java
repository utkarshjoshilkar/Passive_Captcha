package com.passivecaptcha.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Request body for POST /api/v1/score.
 *
 * Contains:
 *  - features: the full computed feature vector
 *  - rawEvents: the raw browser event stream
 *  - label: ground-truth label used during dataset collection
 *
 * Separating the DTO from the entity keeps the API contract
 * decoupled from the persistence model.
 */
public class BehaviorPayload {

    /** The full computed feature vector from the browser */
    private UserFeatures features;
    private String botFamily;
private String botVersion;

    /**
     * Raw event stream for the session.
     * Stored as-is in session_raw_events table for future recomputation.
     * The sessionId on each RawEvent is set server-side after UserFeatures is saved.
     */
    private List<RawEvent> rawEvents = new ArrayList<>();

    /**
     * Ground-truth label for supervised ML.
     * HUMAN -> genuine user session
     * BOT -> automated/scripted session
     * UNKNOWN -> normal application usage (default)
     */
    private DetectionLabel label = DetectionLabel.UNKNOWN;

    public BehaviorPayload() {}

    public UserFeatures getFeatures() {
        return features;
    }

    public void setFeatures(UserFeatures features) {
        this.features = features;
    }

    public List<RawEvent> getRawEvents() {
        return rawEvents;
    }

    public void setRawEvents(List<RawEvent> rawEvents) {
        this.rawEvents = rawEvents;
    }

    public DetectionLabel getLabel() {
        return label;
    }

    public void setLabel(DetectionLabel label) {
        this.label = label;
    }
    public String getBotFamily() {
    return botFamily;
}

public void setBotFamily(String botFamily) {
    this.botFamily = botFamily;
}

public String getBotVersion() {
    return botVersion;
}

public void setBotVersion(String botVersion) {
    this.botVersion = botVersion;
}
}