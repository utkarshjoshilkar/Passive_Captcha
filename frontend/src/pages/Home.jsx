import "./Home.css";

export default function Home() {
  return (
    <>
      {/* Hero Section */}
      <section className="hero">
        <div className="hero-content">
          <h1>Welcome to Passive Captcha</h1>
          <p>Secure your applications with invisible protection.</p>
          <button className="hero-btn">Get Started</button>
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
              <h3>Fast Performance</h3>
              <p>Optimized for speed with minimal resource usage.</p>
            </div>

            <div className="feature-card">
              <h3>Easy Integration</h3>
              <p>Simple setup to secure your forms in minutes.</p>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}