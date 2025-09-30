import { useState, useEffect } from "react";
import "./PassiveCaptchaDemo.css";

export default function PassiveCaptchaDemo() {
  const [userBehavior, setUserBehavior] = useState({
    numPointerMoves: 0,
    avgPointerSpeed: 0,
    usedKeyboard: false,
    sessionDuration: 0,
    startTime: null
  });
  
  const [scoreResult, setScoreResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const startTime = Date.now();
    setUserBehavior(prev => ({ ...prev, startTime }));

    // Track mouse movements
    let moveCount = 0;
    let totalDistance = 0;
    let lastX = 0;
    let lastY = 0;

    const handleMouseMove = (e) => {
      moveCount++;
      
      if (lastX !== 0 && lastY !== 0) {
        const distance = Math.sqrt(Math.pow(e.clientX - lastX, 2) + Math.pow(e.clientY - lastY, 2));
        totalDistance += distance;
      }
      
      lastX = e.clientX;
      lastY = e.clientY;

      const avgSpeed = moveCount > 0 ? totalDistance / moveCount : 0;
      
      setUserBehavior(prev => ({
        ...prev,
        numPointerMoves: moveCount,
        avgPointerSpeed: avgSpeed,
        sessionDuration: Date.now() - startTime
      }));
    };

    // Track keyboard usage
    const handleKeyPress = () => {
      setUserBehavior(prev => ({ ...prev, usedKeyboard: true }));
    };

    document.addEventListener('mousemove', handleMouseMove);
    document.addEventListener('keydown', handleKeyPress);

    return () => {
      document.removeEventListener('mousemove', handleMouseMove);
      document.removeEventListener('keydown', handleKeyPress);
    };
  }, []);

  const submitBehavior = async () => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await fetch('http://localhost:8080/api/v1/score', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userBehavior)
      });

      if (!response.ok) {
        throw new Error('Failed to submit behavior data');
      }

      const result = await response.json();
      setScoreResult(result);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const resetDemo = () => {
    setUserBehavior({
      numPointerMoves: 0,
      avgPointerSpeed: 0,
      usedKeyboard: false,
      sessionDuration: 0,
      startTime: Date.now()
    });
    setScoreResult(null);
    setError(null);
  };

  return (
    <div className="demo-container">
      <div className="demo-content">
        <h1>Passive Captcha Demo</h1>
        <p>Interact with this page normally, then click "Analyze My Behavior" to see your risk score.</p>

        <div className="behavior-tracker">
          <h2>Behavior Tracking</h2>
          <div className="behavior-stats">
            <div className="stat">
              <label>Mouse Movements:</label>
              <span>{userBehavior.numPointerMoves}</span>
            </div>
            <div className="stat">
              <label>Avg Pointer Speed:</label>
              <span>{userBehavior.avgPointerSpeed.toFixed(2)} px/move</span>
            </div>
            <div className="stat">
              <label>Keyboard Used:</label>
              <span>{userBehavior.usedKeyboard ? "Yes" : "No"}</span>
            </div>
            <div className="stat">
              <label>Session Duration:</label>
              <span>{(userBehavior.sessionDuration / 1000).toFixed(1)}s</span>
            </div>
          </div>
        </div>

        <div className="demo-actions">
          <button 
            onClick={submitBehavior} 
            disabled={loading}
            className="analyze-btn"
          >
            {loading ? "Analyzing..." : "Analyze My Behavior"}
          </button>
          
          <button onClick={resetDemo} className="reset-btn">
            Reset Demo
          </button>
        </div>

        {error && (
          <div className="error-message">
            <strong>Error:</strong> {error}
          </div>
        )}

        {scoreResult && (
          <div className={`score-result ${scoreResult.decision === 'allow' ? 'allowed' : 'challenged'}`}>
            <h3>Analysis Result</h3>
            <div className="result-details">
              <div className="score">
                <label>Risk Score:</label>
                <span className="score-value">{scoreResult.score}</span>
                <div className="score-bar">
                  <div 
                    className="score-fill" 
                    style={{ width: `${scoreResult.score * 100}%` }}
                  ></div>
                </div>
              </div>
              <div className="decision">
                <label>Decision:</label>
                <span className={`decision-badge ${scoreResult.decision}`}>
                  {scoreResult.decision.toUpperCase()}
                </span>
              </div>
              <div className="explanation">
                {scoreResult.decision === 'allow' ? 
                  "✅ Your behavior appears human-like. Access granted!" :
                  "⚠️ Your behavior seems suspicious. Additional verification required."
                }
              </div>
            </div>
          </div>
        )}

        <div className="demo-info">
          <h3>How It Works</h3>
          <ul>
            <li>Move your mouse around the page</li>
            <li>Type on your keyboard</li>
            <li>Spend some time on the page</li>
            <li>Click "Analyze My Behavior" to see your risk score</li>
          </ul>
        </div>
      </div>
    </div>
  );
}