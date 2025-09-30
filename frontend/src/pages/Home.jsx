import "./Home.css";
import { Link } from "react-router-dom";

export default function Home() {
  return (
    <>
      {/* Hero Section */}
      <section className="hero">
        <div className="hero-content">
          <h1>Welcome to Passive Captcha</h1>
          <p>Secure your applications with invisible protection.</p>
          <div className="hero-buttons">
            <Link to="/demo" className="hero-btn">Try Demo</Link>
            <Link to="/about" className="hero-btn secondary">Learn More</Link>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features">
        <div className="container">
          <h2>Key Features</h2>
          <div className="features-grid">
            <div className="feature-card">
              <h3>Invisible Protection</h3>
              <p>Protect your site without annoying users.</p>
            </div>

            <div className="feature-card">
              <h3>Behavior Analysis</h3>
              <p>Analyze mouse movements, keyboard usage, and session patterns.</p>
            </div>

            <div className="feature-card">
              <h3>Real-time Scoring</h3>
              <p>Instant risk assessment with detailed analytics.</p>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}