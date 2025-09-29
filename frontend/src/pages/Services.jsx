import "./Services.css";
export default function Services() {
  return (
    <div className="services-container">
      <h1>Our Services</h1>

      <div className="services-grid">
        <div className="service-card">
          <h2>Seamless Verification</h2>
          <p>
            Users don’t need to solve puzzles or click images.
            Verification happens automatically in the background.
          </p>
        </div>

        <div className="service-card">
          <h2>Bot Protection</h2>
          <p>
            Advanced algorithms detect automated scripts and bots
            by analyzing user behavior patterns.
          </p>
        </div>

        <div className="service-card">
          <h2>Easy Integration</h2>
          <p>
            Our system provides simple APIs that can be integrated
            with any website or application quickly.
          </p>
        </div>
      </div>
    </div>
  );
}
