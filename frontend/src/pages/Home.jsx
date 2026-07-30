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
              <Link
                id="view-demo-button"
                to="/demo"
                className="btn btn-primary"
              >
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

      <section className="features-section">
        <div className="container">
          <div className="section-header">
            <h2>Why Choose PassiveCaptcha</h2>
            <p>Modern security that does not compromise user experience</p>
          </div>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">👤</div>
              <h3>Zero User Friction</h3>
              <p>No puzzles, no image challenges, no interruptions. Protection works in the background.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">⚡</div>
              <h3>Fast Integration</h3>
              <p>Install the service, collect behavioral signals, and start scoring requests in minutes.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">🧠</div>
              <h3>Heuristic-Based Intelligence</h3>
              <p>Version 1 uses transparent rules and explainable scoring to make the system easy to understand.</p>
            </div>
          </div>
        </div>
      </section>

      <section className="info-section">
        <div className="container">
          <div className="section-header">
            <h2>How It Works</h2>
            <p>Visitors generate behavioral signals, the backend scores them, and the platform returns a heuristic decision.</p>
          </div>
          <div className="workflow-grid">
            <div className="workflow-card">
              <h3>1. Collect</h3>
              <p>Mouse movement, scrolling, keyboard activity, and session timing are captured anonymously.</p>
            </div>
            <div className="workflow-card">
              <h3>2. Score</h3>
              <p>The backend evaluates these signals using a transparent heuristic engine.</p>
            </div>
            <div className="workflow-card">
              <h3>3. Decide</h3>
              <p>The platform returns allow, review, or challenge based on the score.</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}