import "./Footer.css";

export default function Footer() {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-content">
          <div className="footer-section">
            <h3>Passive Captcha</h3>
            <p>Making the web safer and more user-friendly.</p>
          </div>
          
          <div className="footer-section">
            <h4>Quick Links</h4>
            <div className="footer-links">
              <a href="/">Home</a>
              <a href="/about">About</a>
              <a href="/services">Services</a>
              <a href="/stats">Stats</a>
            </div>
          </div>
          
          <div className="footer-section">
            <h4>Contact</h4>
            <p>Email: info@passivecaptcha.com</p>
            <p>Support: support@passivecaptcha.com</p>
          </div>
        </div>
        
        <div className="footer-bottom">
          <p>&copy; 2025 Passive Captcha. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}