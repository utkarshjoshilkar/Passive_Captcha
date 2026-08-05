import { useState, useEffect, useRef, useCallback } from "react";
import "./Demo.css";

// ─── Constants ──────────────────────────────────────────────────────────────
const API_URL = "http://localhost:8080/api/v1/score";
const DATASET_LABEL = "HUMAN";
const BOT_FAMILY = null;
const BOT_VERSION = null;
const IDLE_THRESHOLD = 1000;  // ms gap → idle
const HESIT_SPEED = 0.1;   // px/ms → hesitation threshold
const HESIT_DURATION = 100;   // ms below threshold → hesitation counted
const DIR_CHANGE_DEG = 45;    // degrees → direction change threshold
const DOUBLE_CLICK_MS = 300;   // ms between clicks → double-click
const MAX_RAW_EVENTS = 5000;  // cap raw events to avoid huge payloads

// ─── Math helpers ────────────────────────────────────────────────────────────
const dist2D = (x1, y1, x2, y2) => Math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2);
const mean = (arr) => arr.length ? arr.reduce((s, v) => s + v, 0) / arr.length : 0;
const variance = (arr) => {
  if (arr.length < 2) return 0;
  const m = mean(arr);
  return mean(arr.map(v => (v - m) ** 2));
};
const shannonEntropy = (values, bins = 10) => {
  if (!values.length) return 0;
  const min = Math.min(...values), max = Math.max(...values);
  const range = max - min || 1;
  const counts = new Array(bins).fill(0);
  values.forEach(v => {
    const b = Math.min(Math.floor(((v - min) / range) * bins), bins - 1);
    counts[b]++;
  });
  const n = values.length;
  return -counts.filter(c => c > 0).reduce((s, c) => s + (c / n) * Math.log2(c / n), 0);
};

// ─── Feature computation from accumulated state ───────────────────────────
function computeFeatures(state, sessionStart) {
  const now = Date.now();
  const duration = now - sessionStart;

  const {
    mouseSpeeds, mouseAccels, mouseJerks, mouseAngles,
    totalDist, startX, startY, endX, endY,
    dirChanges, hesitations, idleMs,
    clicks, clickPositions, lastClickTime,
    scrollDistances, scrollSpeeds, scrollPauses, scrollDirChanges,
    keys, lastKeyTime, backspaces, pastes, copies,
    focusChanges, visChanges, resizes, rightClicks,
  } = state;

  const numMoves = mouseSpeeds.length;

  // ── Mouse ──────────────────────────────────────────────────────────────────
  const avgSpeed = mean(mouseSpeeds);
  const maxSpeed = mouseSpeeds.length ? Math.max(...mouseSpeeds) : 0;
  const speedVar = variance(mouseSpeeds);
  const avgAcc = mean(mouseAccels);
  const maxAcc = mouseAccels.length ? Math.max(...mouseAccels) : 0;
  const avgJerk = mean(mouseJerks);

  // Curvature: average turning angle
  let curvature = 0;
  if (mouseAngles.length >= 2) {
    let totalTurn = 0;
    for (let i = 1; i < mouseAngles.length; i++) {
      let diff = Math.abs(mouseAngles[i] - mouseAngles[i - 1]);
      if (diff > Math.PI) diff = 2 * Math.PI - diff;
      totalTurn += diff;
    }
    curvature = totalTurn / (mouseAngles.length - 1);
  }

  const directDist = dist2D(startX, startY, endX, endY);
  const straightness = totalDist > 0 ? Math.min(directDist / totalDist, 1) : 0;
  const mouseEntropy = shannonEntropy(mouseSpeeds);

  // ── Clicks ─────────────────────────────────────────────────────────────────
  const clickIntervals = [];
  for (let i = 1; i < clicks.length; i++) clickIntervals.push(clicks[i] - clicks[i - 1]);
  const avgClickInterval = mean(clickIntervals);
  let dblClicks = 0;
  clickIntervals.forEach(g => { if (g <= DOUBLE_CLICK_MS) dblClicks++; });

  const cxVar = variance(clickPositions.map(p => p.x));
  const cyVar = variance(clickPositions.map(p => p.y));
  const clickPosVar = (cxVar + cyVar) / 2;

  // ── Scroll ─────────────────────────────────────────────────────────────────
  const avgScrollDist = mean(scrollDistances);
  const avgScrollSpeed = mean(scrollSpeeds);
  const scrollSpeedVar = variance(scrollSpeeds);
  const scrollEntropy = shannonEntropy(scrollDistances);
  const scrollPause = mean(scrollPauses);

  // ── Keyboard ──────────────────────────────────────────────────────────────
  const keyCount = keys.length;
  const backRatio = keyCount > 0 ? backspaces / keyCount : 0;
  const typingSpeed = duration > 0 ? keyCount / (duration / 1000) : 0;
  const keyIntervals = [];
  for (let i = 1; i < keys.length; i++) keyIntervals.push(keys[i] - keys[i - 1]);
  const avgKeyInterval = mean(keyIntervals);

  // ── Statistical ────────────────────────────────────────────────────────────
  const totalEvents = numMoves + clicks.length + scrollDistances.length + keyCount;
  const density = duration > 0 ? totalEvents / (duration / 1000) : 0;
  const otherEvents = clicks.length + scrollDistances.length + keyCount;
  const eventRatio = otherEvents > 0 ? numMoves / otherEvents : numMoves;
  const speedEntropy = shannonEntropy(mouseSpeeds);

  return {
    numPointerMoves: numMoves,
    totalPointerDistance: Math.round(totalDist * 100) / 100,
    avgPointerSpeed: Math.round(avgSpeed * 10000) / 10000,
    maxPointerSpeed: Math.round(maxSpeed * 10000) / 10000,
    speedVariance: Math.round(speedVar * 10000) / 10000,
    avgPointerAcceleration: Math.round(avgAcc * 10000) / 10000,
    maxPointerAcceleration: Math.round(maxAcc * 10000) / 10000,
    avgPointerJerk: Math.round(avgJerk * 10000) / 10000,
    pathCurvature: Math.round(curvature * 10000) / 10000,
    straightnessRatio: Math.round(straightness * 10000) / 10000,
    mouseDirectionChanges: dirChanges,
    hesitationCount: hesitations,
    idleTimeMs: idleMs,
    mouseEntropy: Math.round(mouseEntropy * 10000) / 10000,

    clickCount: clicks.length,
    doubleClickCount: dblClicks,
    avgClickIntervalMs: Math.round(avgClickInterval),
    clickPositionVariance: Math.round(clickPosVar),
    rightClickCount: rightClicks,

    numScrolls: scrollDistances.length,
    scrollDirectionChanges: scrollDirChanges,
    avgScrollDistance: Math.round(avgScrollDist * 100) / 100,
    avgScrollSpeed: Math.round(avgScrollSpeed * 10000) / 10000,
    scrollSpeedVariance: Math.round(scrollSpeedVar * 10000) / 10000,
    scrollEntropy: Math.round(scrollEntropy * 10000) / 10000,
    scrollPauseTimeMs: Math.round(scrollPause),

    usedKeyboard: keyCount > 0,
    keyPressCount: keyCount,
    avgKeyIntervalMs: Math.round(avgKeyInterval),
    typingSpeed: Math.round(typingSpeed * 100) / 100,
    backspaceCount: backspaces,
    backspaceRatio: Math.round(backRatio * 10000) / 10000,
    pasteEventCount: pastes,
    copyEventCount: copies,

    focusChanges: focusChanges,
    visibilityChanges: visChanges,
    windowResizeCount: resizes,

    speedEntropy: Math.round(speedEntropy * 10000) / 10000,
    interactionDensity: Math.round(density * 100) / 100,
    eventRatio: Math.round(eventRatio * 100) / 100,

    sessionDuration: duration,
  };
}

// ─── Component ───────────────────────────────────────────────────────────────
export default function Demo() {
  const sessionStart = useRef(Date.now());
  const rawEvents = useRef([]);
  const stateRef = useRef({
    mouseSpeeds: [], mouseAccels: [], mouseJerks: [], mouseAngles: [],
    totalDist: 0, startX: 0, startY: 0, endX: 0, endY: 0,
    lastX: null, lastY: null, lastT: null, lastSpeed: null, lastAcc: null,
    lastAngle: null, dirChanges: 0, hesitations: 0, idleMs: 0,
    hesStart: -1,
    clicks: [], clickPositions: [], lastClickTime: null,
    scrollDistances: [], scrollSpeeds: [], scrollPauses: [],
    scrollDirChanges: 0, lastScrollT: null, lastScrollDir: null,
    keys: [], backspaces: 0, pastes: 0, copies: 0,
    focusChanges: 0, visChanges: 0, resizes: 0, rightClicks: 0,
  });

  const [features, setFeatures] = useState(null);
  const [scoreResult, setScoreResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState("mouse");
  const [feedback, setFeedback] = useState("");
  const [userName, setUserName] = useState("");
  const [rating, setRating] = useState(0);

  const pushRaw = useCallback((evt) => {
    if (rawEvents.current.length < MAX_RAW_EVENTS)
      rawEvents.current.push(evt);
  }, []);

  // ── Update live display every 500 ms ──────────────────────────────────────
  useEffect(() => {
    const iv = setInterval(() => {
      setFeatures(computeFeatures(stateRef.current, sessionStart.current));
    }, 500);
    return () => clearInterval(iv);
  }, []);

  // ── Event listeners ────────────────────────────────────────────────────────
  useEffect(() => {
    const s = stateRef.current;

    // MOUSE MOVE
    const onMouseMove = (e) => {
      const t = e.timeStamp;
      const x = e.clientX, y = e.clientY;

      pushRaw({ type: "mousemove", x, y, t });

      if (s.lastX === null) {
        s.startX = x; s.startY = y;
      }
      s.endX = x; s.endY = y;

      if (s.lastX !== null) {
        const dt = t - s.lastT;
        if (dt > 0) {
          const dx = x - s.lastX, dy = y - s.lastY;
          const d = Math.sqrt(dx * dx + dy * dy);
          s.totalDist += d;
          const spd = d / dt;
          s.mouseSpeeds.push(spd);

          // Idle accumulation
          if (dt > IDLE_THRESHOLD) s.idleMs += dt;

          // Hesitation
          if (spd < HESIT_SPEED) {
            if (s.hesStart < 0) s.hesStart = s.lastT;
          } else {
            if (s.hesStart >= 0) {
              if (t - s.hesStart >= HESIT_DURATION) s.hesitations++;
              s.hesStart = -1;
            }
          }

          // Angle & direction change
          const ang = Math.atan2(dy, dx);
          s.mouseAngles.push(ang);
          if (s.lastAngle !== null) {
            let diff = Math.abs(ang - s.lastAngle);
            if (diff > Math.PI) diff = 2 * Math.PI - diff;
            if (diff > DIR_CHANGE_DEG * Math.PI / 180) s.dirChanges++;
          }
          s.lastAngle = ang;

          // Acceleration
          if (s.lastSpeed !== null) {
            const acc = Math.abs(spd - s.lastSpeed) / dt;
            s.mouseAccels.push(acc);

            // Jerk
            if (s.lastAcc !== null) {
              s.mouseJerks.push(Math.abs(acc - s.lastAcc) / dt);
            }
            s.lastAcc = acc;
          }
          s.lastSpeed = spd;
        }
      }
      s.lastX = x; s.lastY = y; s.lastT = t;
    };

    // CLICK
    const onClick = (e) => {
      const t = e.timeStamp;
      pushRaw({ type: "click", x: e.clientX, y: e.clientY, t });
      s.clicks.push(t);
      s.clickPositions.push({ x: e.clientX, y: e.clientY });
      s.lastClickTime = t;
    };

    // RIGHT CLICK
    const onContextMenu = (e) => {
      pushRaw({ type: "rightclick", x: e.clientX, y: e.clientY, t: e.timeStamp });
      s.rightClicks++;
    };

    // SCROLL
    const onScroll = () => {
      const t = Date.now();
      const curY = window.scrollY;

      if (s._lastScrollY === undefined) s._lastScrollY = curY;
      const dy = curY - s._lastScrollY;
      const absDy = Math.abs(dy);
      pushRaw({ type: "scroll", dy, t });

      s.scrollDistances.push(absDy);
      const dir = dy >= 0 ? "down" : "up";
      if (s.lastScrollDir && dir !== s.lastScrollDir) s.scrollDirChanges++;
      s.lastScrollDir = dir;

      if (s.lastScrollT !== null) {
        const dt = t - s.lastScrollT;
        if (dt > 0) {
          s.scrollSpeeds.push(absDy / dt);
          s.scrollPauses.push(dt);
        }
      }
      s.lastScrollT = t;
      s._lastScrollY = curY;
    };

    // KEY
    const onKeyDown = (e) => {
      const t = e.timeStamp;
      pushRaw({ type: "keydown", key: e.key, t });
      s.keys.push(t);
      if (e.key === "Backspace" || e.key === "Delete") s.backspaces++;
      if ((e.ctrlKey || e.metaKey) && e.key === "c") { s.copies++; pushRaw({ type: "copy", t }); }
      if ((e.ctrlKey || e.metaKey) && e.key === "v") { s.pastes++; pushRaw({ type: "paste", t }); }
    };

    // PASTE / COPY events
    const onPaste = (e) => { pushRaw({ type: "paste", t: Date.now() }); s.pastes++; };
    const onCopy = (e) => { pushRaw({ type: "copy", t: Date.now() }); s.copies++; };

    // FOCUS / BLUR
    const onFocus = () => { pushRaw({ type: "focus", t: Date.now() }); s.focusChanges++; };
    const onBlur = () => { pushRaw({ type: "blur", t: Date.now() }); s.focusChanges++; };

    // VISIBILITY
    const onVis = () => { pushRaw({ type: "visibilitychange", t: Date.now() }); s.visChanges++; };

    // RESIZE
    const onResize = () => { pushRaw({ type: "resize", t: Date.now() }); s.resizes++; };

    document.addEventListener("mousemove", onMouseMove, { passive: true });
    document.addEventListener("click", onClick, { passive: true });
    document.addEventListener("contextmenu", onContextMenu, { passive: true });
    window.addEventListener("scroll", onScroll, { passive: true });
    document.addEventListener("keydown", onKeyDown);
    document.addEventListener("paste", onPaste);
    document.addEventListener("copy", onCopy);
    window.addEventListener("focus", onFocus);
    window.addEventListener("blur", onBlur);
    document.addEventListener("visibilitychange", onVis);
    window.addEventListener("resize", onResize, { passive: true });

    return () => {
      document.removeEventListener("mousemove", onMouseMove);
      document.removeEventListener("click", onClick);
      document.removeEventListener("contextmenu", onContextMenu);
      window.removeEventListener("scroll", onScroll);
      document.removeEventListener("keydown", onKeyDown);
      document.removeEventListener("paste", onPaste);
      document.removeEventListener("copy", onCopy);
      window.removeEventListener("focus", onFocus);
      window.removeEventListener("blur", onBlur);
      document.removeEventListener("visibilitychange", onVis);
      window.removeEventListener("resize", onResize);
    };
  }, [pushRaw]);

  // ── Submit ─────────────────────────────────────────────────────────────────
  const submitBehavior = async () => {
    setLoading(true);
    setError(null);
    const currentFeatures = computeFeatures(stateRef.current, sessionStart.current);
    try {

      const res = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },


        body: JSON.stringify({
          features: currentFeatures,
          rawEvents: rawEvents.current,

          label: DATASET_LABEL,
          botFamily: BOT_FAMILY,
          botVersion: BOT_VERSION
        }),
      });
      if (!res.ok) throw new Error("Server returned " + res.status);
      setScoreResult(await res.json());
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // ── Reset ──────────────────────────────────────────────────────────────────
  const resetDemo = () => {
    sessionStart.current = Date.now();
    rawEvents.current = [];
    const s = stateRef.current;
    Object.assign(s, {
      mouseSpeeds: [], mouseAccels: [], mouseJerks: [], mouseAngles: [],
      totalDist: 0, startX: 0, startY: 0, endX: 0, endY: 0,
      lastX: null, lastY: null, lastT: null, lastSpeed: null, lastAcc: null,
      lastAngle: null, dirChanges: 0, hesitations: 0, idleMs: 0, hesStart: -1,
      clicks: [], clickPositions: [], lastClickTime: null,
      scrollDistances: [], scrollSpeeds: [], scrollPauses: [],
      scrollDirChanges: 0, lastScrollT: null, lastScrollDir: null,
      _lastScrollY: undefined,
      keys: [], backspaces: 0, pastes: 0, copies: 0,
      focusChanges: 0, visChanges: 0, resizes: 0, rightClicks: 0,
    });
    setFeatures(null);
    setScoreResult(null);
    setError(null);
  };

  const f = features || {};

  const TABS = [
    { id: "mouse", label: "🖱️ Mouse" },
    { id: "click", label: "🖱️ Clicks" },
    { id: "scroll", label: "📜 Scroll" },
    { id: "keyboard", label: "⌨️ Keyboard" },
    { id: "browser", label: "🌐 Browser" },
    { id: "stats", label: "📊 Stats" },
  ];

  const FEATURE_GROUPS = {
    mouse: [
      ["Mouse Moves", f.numPointerMoves, ""],
      ["Total Distance", (f.totalPointerDistance || 0).toFixed(0), "px"],
      ["Avg Speed", (f.avgPointerSpeed || 0).toFixed(4), "px/ms"],
      ["Max Speed", (f.maxPointerSpeed || 0).toFixed(4), "px/ms"],
      ["Speed Variance", (f.speedVariance || 0).toFixed(6), ""],
      ["Avg Acceleration", (f.avgPointerAcceleration || 0).toFixed(6), "px/ms²"],
      ["Max Acceleration", (f.maxPointerAcceleration || 0).toFixed(6), "px/ms²"],
      ["Avg Jerk", (f.avgPointerJerk || 0).toFixed(8), "px/ms³"],
      ["Path Curvature", (f.pathCurvature || 0).toFixed(4), "rad"],
      ["Straightness Ratio", (f.straightnessRatio || 0).toFixed(4), ""],
      ["Direction Changes", f.mouseDirectionChanges, ""],
      ["Hesitation Count", f.hesitationCount, ""],
      ["Idle Time", f.idleTimeMs, "ms"],
      ["Mouse Entropy", (f.mouseEntropy || 0).toFixed(4), "bits"],
    ],
    click: [
      ["Click Count", f.clickCount, ""],
      ["Double Clicks", f.doubleClickCount, ""],
      ["Avg Click Interval", f.avgClickIntervalMs, "ms"],
      ["Click Pos Variance", (f.clickPositionVariance || 0).toFixed(1), "px²"],
      ["Right Clicks", f.rightClickCount, ""],
    ],
    scroll: [
      ["Scroll Events", f.numScrolls, ""],
      ["Direction Changes", f.scrollDirectionChanges, ""],
      ["Avg Scroll Distance", (f.avgScrollDistance || 0).toFixed(1), "px"],
      ["Avg Scroll Speed", (f.avgScrollSpeed || 0).toFixed(4), "px/ms"],
      ["Scroll Speed Variance", (f.scrollSpeedVariance || 0).toFixed(6), ""],
      ["Scroll Entropy", (f.scrollEntropy || 0).toFixed(4), "bits"],
      ["Avg Pause Between Scrolls", (f.scrollPauseTimeMs || 0).toFixed(0), "ms"],
    ],
    keyboard: [
      ["Keyboard Used", f.usedKeyboard ? "Yes" : "No", ""],
      ["Key Presses", f.keyPressCount, ""],
      ["Avg Key Interval", f.avgKeyIntervalMs, "ms"],
      ["Typing Speed", (f.typingSpeed || 0).toFixed(2), "keys/s"],
      ["Backspace Count", f.backspaceCount, ""],
      ["Backspace Ratio", (f.backspaceRatio || 0).toFixed(4), ""],
      ["Paste Events", f.pasteEventCount, ""],
      ["Copy Events", f.copyEventCount, ""],
    ],
    browser: [
      ["Focus Changes", f.focusChanges, ""],
      ["Visibility Changes", f.visibilityChanges, ""],
      ["Window Resizes", f.windowResizeCount, ""],
    ],
    stats: [
      ["Session Duration", ((f.sessionDuration || 0) / 1000).toFixed(1), "s"],
      ["Speed Entropy", (f.speedEntropy || 0).toFixed(4), "bits"],
      ["Interaction Density", (f.interactionDensity || 0).toFixed(2), "events/s"],
      ["Event Ratio", (f.eventRatio || 0).toFixed(2), "moves/other"],
      ["Raw Events Captured", rawEvents.current.length, ""],
    ],
  };

  const decisionColor = {
    allow: "#10b981", review: "#f59e0b", challenge: "#ef4444"
  };

  return (
    <div className="demo-page">
      <div className="demo-container">
        <div className="demo-content">
          <h1>Passive Captcha Demo</h1>
          <p>Interact naturally with this page — move your mouse, scroll, type, click. Then analyze!</p>

          {/* ── Feature Tracker ──────────────────────────────────────────────── */}
          <div className="behavior-tracker">
            <div className="tracker-header">
              <h2>📡 Live Behavioral Telemetry</h2>
              <span className="raw-count">
                📦 {rawEvents.current.length} raw events
              </span>
            </div>

            {/* Tab bar */}
            <div className="feature-tabs">
              {TABS.map(tab => (
                <button
                  key={tab.id}
                  className={`feature-tab ${activeTab === tab.id ? "active" : ""}`}
                  onClick={() => setActiveTab(tab.id)}
                >
                  {tab.label}
                </button>
              ))}
            </div>

            {/* Feature table */}
            <div className="feature-panel">
              {(FEATURE_GROUPS[activeTab] || []).map(([label, value, unit]) => (
                <div className="feature-row" key={label}>
                  <span className="feature-label">{label}</span>
                  <span className="feature-value">
                    {value ?? 0}
                    {unit ? <em>{unit}</em> : null}
                  </span>
                </div>
              ))}
            </div>
          </div>

          {/* ── Actions ──────────────────────────────────────────────────────── */}
          <div className="demo-actions">
            <button id="analyze-button" onClick={submitBehavior} disabled={loading} className="analyze-btn">
              {loading ? "Analyzing…" : "🔍 Analyze My Behavior"}
            </button>
            <button onClick={resetDemo} className="reset-btn">
              🔄 Reset
            </button>
          </div>

          {/* ── Feedback Section ───────────────────────────────────────────── */}

          <div className="feedback-card">

            <h2>💬 Tell Us About Your Experience</h2>

            <p>
              Your feedback helps us improve Passive CAPTCHA.
              Please describe your experience while interacting
              with this demo.
            </p>
            <div className="feedback-input-group">

              <label>Your Name</label>

              <input
                id="user-name"
                type="text"
                value={userName}
                onChange={(e) => setUserName(e.target.value)}
                placeholder="Enter your name"
                maxLength={50}
              />

            </div>
            <div className="feedback-input-group">

              <label>Overall Experience</label>

              <div className="rating-stars">

                {[1, 2, 3, 4, 5].map((star) => (
                  <span
                    id={`rating-${star}`}
                    key={star}
                    className={star <= rating ? "star active" : "star"}
                    onClick={() => setRating(star)}
                  >
                    ★
                  </span>
                ))}

              </div>

            </div>

            <textarea
              id="feedback"
              value={feedback}
              onChange={(e) => setFeedback(e.target.value)}
              placeholder="Share your experience here..."
              rows={6}
              maxLength={500}
            />

            <div className="feedback-footer">

              <span>
                {feedback.length}/500 characters
                {feedback.length < 30 && " (Minimum 30 characters)"}
              </span>

              <button
                id="submit-feedback"
                className="submit-feedback-btn"
                disabled={feedback.length < 30}
                onClick={() => alert("Thank you for your feedback!")}
              >
                Submit Feedback
              </button>

            </div>

          </div>

          {/* ── Error ────────────────────────────────────────────────────────── */}
          {error && (
            <div className="error-message">
              <strong>⚠️ Error:</strong> {error}
            </div>
          )}

          {/* ── Score Result ─────────────────────────────────────────────────── */}
          {scoreResult && (
            <div className="score-result" style={{
              borderLeftColor: decisionColor[scoreResult.decision] || "#6b7280"
            }}>
              <h3>Analysis Result</h3>
              <div className="result-grid">
                <div className="result-main">
                  <div className="result-score-label">Human Score</div>
                  <div className="result-score-value"
                    style={{ color: decisionColor[scoreResult.decision] }}>
                    {scoreResult.score}
                  </div>
                  <div className="score-bar">
                    <div className="score-fill"
                      style={{ width: `${Math.max(4, scoreResult.score * 100)}%` }} />
                  </div>
                </div>
                <div className="result-badges">
                  <div className="result-field">
                    <span className="rf-label">Decision</span>
                    <span className={`decision-badge ${scoreResult.decision}`}>
                      {scoreResult.decision?.toUpperCase()}
                    </span>
                  </div>
                  <div className="result-field">
                    <span className="rf-label">Risk</span>
                    <span className={`decision-badge ${scoreResult.risk === "low" ? "allow" :
                      scoreResult.risk === "medium" ? "review" : "challenge"
                      }`}>
                      {scoreResult.risk?.toUpperCase()}
                    </span>
                  </div>
                  <div className="result-field">
                    <span className="rf-label">Confidence</span>
                    <span className="rf-value">{scoreResult.confidence}</span>
                  </div>
                  {scoreResult.rawEventCount !== undefined && (
                    <div className="result-field">
                      <span className="rf-label">Raw Events Stored</span>
                      <span className="rf-value">{scoreResult.rawEventCount}</span>
                    </div>
                  )}
                </div>
              </div>

              {/* Feature breakdown */}
              {scoreResult.featureBreakdown && (
                <div className="breakdown-section">
                  <h4>Feature Sub-scores</h4>
                  <div className="breakdown-grid">
                    {Object.entries(scoreResult.featureBreakdown).map(([key, val]) => (
                      <div className="breakdown-row" key={key}>
                        <span className="bd-label">
                          {key.replace(/([A-Z])/g, " $1").trim()}
                        </span>
                        <div className="bd-bar-wrap">
                          <div className="bd-bar"
                            style={{ width: `${Math.min(val, 1) * 100}%` }} />
                        </div>
                        <span className="bd-val">{val}</span>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              <div className="explanation">
                {scoreResult.message}
              </div>
            </div>
          )}

          {/* ── How it works ─────────────────────────────────────────────────── */}
          <div className="demo-info">
            <h3>How It Works</h3>
            <ul>
              <li>Move your mouse — tracks speed, acceleration, jerk, curvature</li>
              <li>Scroll the page — measures scroll entropy and speed variance</li>
              <li>Type anything — captures rhythm, backspaces, typing speed</li>
              <li>Click around — measures click interval and position variance</li>
              <li>All events are timestamped and sent as a raw stream to the backend</li>
              <li>35 behavioral features are computed and scored by the heuristic engine</li>
            </ul>
            <p className="demo-note">
              <strong>📊 ML Ready:</strong> Download the full dataset as CSV via{" "}
              <code>GET /api/v1/export/csv</code> for use with scikit-learn, Weka, or R.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}