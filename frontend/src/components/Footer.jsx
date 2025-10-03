import { Link } from "react-router-dom";
import "./Footer.css";

export default function Footer() {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-content">
          <div className="footer-brand">
            <h3>🛡️ PassiveCaptcha</h3>
            <p>Invisible bot protection for modern applications</p>
          </div>
          
          <div className="footer-links">
            <Link to="/" className="footer-link">Home</Link>
            <Link to="/demo" className="footer-link">Demo</Link>
            <Link to="/contact" className="footer-link">Contact</Link>
          </div>
        </div>
        
        <div className="footer-bottom">
          <p>&copy; 2024 PassiveCaptcha. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}