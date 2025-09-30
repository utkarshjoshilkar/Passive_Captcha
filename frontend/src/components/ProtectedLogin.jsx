import { useState, useEffect } from 'react';
import './ProtectedLogin.css';

export default function ProtectedLogin() {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [userBehavior, setUserBehavior] = useState({
    numPointerMoves: 0,
    avgPointerSpeed: 0,
    usedKeyboard: false,
    sessionDuration: 0,
    startTime: Date.now()
  });
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [captchaResult, setCaptchaResult] = useState(null);

  // Track user behavior
  useEffect(() => {
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
        sessionDuration: Date.now() - prev.startTime
      }));
    };

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

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const analyzeBehavior = async () => {
    setIsAnalyzing(true);
    try {
      const response = await fetch('http://localhost:8080/api/v1/score', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userBehavior)
      });

      const result = await response.json();
      setCaptchaResult(result);
      return result;
    } catch (error) {
      console.error('Error analyzing behavior:', error);
      return null;
    } finally {
      setIsAnalyzing(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Step 1: Analyze user behavior
    const analysis = await analyzeBehavior();
    
    if (!analysis) {
      alert('Security check failed. Please try again.');
      return;
    }

    // Step 2: Check if behavior is human-like
    if (analysis.decision === 'challenge') {
      alert(`Suspicious behavior detected (Score: ${analysis.score}). Please complete additional verification.`);
      // Here you could show a traditional CAPTCHA
      return;
    }

    // Step 3: If behavior is human-like, proceed with login
    console.log('Behavior analysis passed! Proceeding with login...');
    console.log('Login data:', formData);
    
    // Here you would normally send the login request to your backend
    alert(`Login successful! Welcome ${formData.email}`);
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h2>User Login</h2>
        <p className="login-description">
          <strong>Passive Captcha Active:</strong> Your behavior is being analyzed in the background
        </p>
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email:</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              required
              placeholder="Enter your email"
            />
          </div>

          <div className="form-group">
            <label>Password:</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              required
              placeholder="Enter your password"
            />
          </div>

          <button 
            type="submit" 
            className="login-btn"
            disabled={isAnalyzing}
          >
            {isAnalyzing ? 'Analyzing Behavior...' : 'Login'}
          </button>
        </form>

        {captchaResult && (
          <div className={`captcha-feedback ${captchaResult.decision}`}>
            <h4>Security Check:</h4>
            <p>
              {captchaResult.decision === 'allow' 
                ? '✅ Human behavior confirmed' 
                : '⚠️ Suspicious activity detected'
              }
            </p>
            <small>Score: {captchaResult.score} | Decision: {captchaResult.decision}</small>
          </div>
        )}

        <div className="behavior-info">
          <h4>How it works:</h4>
          <ul>
            <li>Move your mouse naturally while filling the form</li>
            <li>Type normally using your keyboard</li>
            <li>Take your time (2+ seconds recommended)</li>
            <li>No puzzles to solve - protection happens invisibly</li>
          </ul>
        </div>
      </div>
    </div>
  );
}