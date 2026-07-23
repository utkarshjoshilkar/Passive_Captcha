import React from 'react';
import { Link } from 'react-router-dom';
import './Landing.css';

const Landing = () => {
  return (
    <div className="landing-container">
      <section className="hero-section">
        <div className="hero-content fade-in">
          <h1 className="hero-title">
            Security That <span className="highlight">Doesn't Get In The Way</span>
          </h1>
          <p className="hero-subtitle">
            Distinguish humans from bots with passive behavioral biometrics. 
            No more traffic lights. No more frustration.
          </p>
          <div className="cta-group">
            <Link to="/signup" className="btn btn-primary">Get Started</Link>
            <Link to="/demo" className="btn btn-secondary">Live Demo</Link>
          </div>
        </div>
        <div className="hero-visual fade-in">
          <div className="visual-circle"></div>
          <div className="visual-card glass-panel">
            <div className="status-indicator">
              <span className="dot"></span> Verified Human
            </div>
            <div className="score-display">
              <span className="score-label">Trust Score</span>
              <span className="score-value">0.98</span>
            </div>
          </div>
        </div>
      </section>

      <section className="features-section">
        <h2 className="section-title">Why PassiveCAPTCHA?</h2>
        <div className="features-grid">
          <div className="feature-card glass-panel">
            <h3>Invisible Protection</h3>
            <p>Analyzes mouse movements and keystrokes in the background.</p>
          </div>
          <div className="feature-card glass-panel">
            <h3>Privacy First</h3>
            <p>GDPR compliant. We analyze behavior patterns, not personal data.</p>
          </div>
          <div className="feature-card glass-panel">
            <h3>Easy Integration</h3>
            <p>Add one line of code to your website and you're protected.</p>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Landing;
