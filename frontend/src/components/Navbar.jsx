import { Link } from "react-router-dom";
import { useState } from "react";
import "./Navbar.css";

export default function Navbar() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-logo">Passive Captcha</div>

        <button className="menu-toggle" onClick={() => setIsOpen(!isOpen)}>
          ☰
        </button>

        <ul className={`nav-links ${isOpen ? "open" : ""}`}>
          <li><Link to="/" onClick={() => setIsOpen(false)}>Home</Link></li>
          <li><Link to="/demo" onClick={() => setIsOpen(false)}>Demo</Link></li>
          <li><Link to="/about" onClick={() => setIsOpen(false)}>About</Link></li>
          <li><Link to="/services" onClick={() => setIsOpen(false)}>Services</Link></li>
          <li><Link to="/stats" onClick={() => setIsOpen(false)}>Stats</Link></li>
          {/* <li><Link to="/admin" onClick={() => setIsOpen(false)}>Admin</Link></li> */}
          <li><Link to="/login" onClick={() => setIsOpen(false)}>Protected Login</Link></li>
        </ul>
      </div>
    </nav>
  );
}