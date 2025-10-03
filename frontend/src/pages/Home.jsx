import { Link } from "react-router-dom";
import "./Home.css";

export default function Home() {
  return (
    <div className="home-page">
      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-container">
          <div className="hero-content">
            <h1 className="hero-title">
              PassiveCaptcha: Stop Bots. <span className="highlight">Not Users.</span>
            </h1>
            <p className="hero-subtitle">
              Invisible bot protection that works in the background. No puzzles, no challenges, 
              just powerful security that your users will never see.
            </p>
            <div className="hero-buttons">
              <Link to="/demo" className="btn btn-primary">
                View Demo
              </Link>
              <Link to="/contact" className="btn btn-secondary">
                Contact Us
              </Link>
            </div>
            <div className="hero-stats">
              <div className="stat-item">
                <div className="stat-number">99.8%</div>
                <div className="stat-label">Bot Detection</div>
              </div>
              <div className="stat-item">
                <div className="stat-number">0s</div>
                <div className="stat-label">User Friction</div>
              </div>
              <div className="stat-item">
                <div className="stat-number">5min</div>
                <div className="stat-label">Setup Time</div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="container">
          <div className="section-header">
            <h2>Why Choose PassiveCaptcha</h2>
            <p>Modern security that doesn't compromise user experience</p>
          </div>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">👤</div>
              <h3>Zero User Friction</h3>
              <p>No puzzles to solve, no images to click. Protection happens invisibly in the background.</p>
            </div>
            {/* <div className="feature-card">
              <div className="feature-icon">🤖</div>
              <h3>AI-Powered Detection</h3>
              <p>Advanced machine learning analyzes behavior to detect bots with 99.8% accuracy.</p>
            </div> */}
            <div className="feature-card">
              <div className="feature-icon">⚡</div>
              <h3>Easy Integration</h3>
              <p>Add powerful bot protection with just a few lines of code in minutes.</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}