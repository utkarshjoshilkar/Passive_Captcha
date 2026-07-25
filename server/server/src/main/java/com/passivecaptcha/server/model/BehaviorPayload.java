package com.passivecaptcha.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Request body for POST /api/v1/score.
 *
 * Contains:
 *  - features: the full computed feature vector (35+ fields)
 *  - rawEvents: the raw browser event stream for this session
 *
 * Separating the DTO from the entity keeps the API contract
 * decoupled from the persistence model.
 */
public class BehaviorPayload {

    /** The full computed feature vector from the browser */
    private UserFeatures features;

    /**
     * Raw event stream for the session.
     * Stored as-is in session_raw_events table for future recomputation.
     * The sessionId on each RawEvent is set server-side after UserFeatures is saved.
     */
    private List<RawEvent> rawEvents = new ArrayList<>();

    public BehaviorPayload() {}

    public UserFeatures getFeatures()              { return features; }
    public void         setFeatures(UserFeatures v){ this.features = v; }

    public List<RawEvent> getRawEvents()           { return rawEvents; }
    public void           setRawEvents(List<RawEvent> v){ this.rawEvents = v; }
}
