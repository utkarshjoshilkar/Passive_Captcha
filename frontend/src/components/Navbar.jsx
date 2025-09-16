import {Link, NavLink} from "react-router-dom";
import "./Navbar.css";
import { useState } from "react";

export default function Navbar(){

    const[isOpen, setIsOpen] = useState(false);

    return (
        <nav className="navbar">
            <div className="navbar-logo">Passive Captcha</div>

            <button className="menu-toggle" onClick={()=> setIsOpen(!isOpen)}>☰</button>

            <ul className={`nav-links ${isOpen ? "open" : ""}`}>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/about">About</Link></li>
                <li><Link to="/services">Services</Link></li>
                <li><Link to="/stats">Stats</Link></li>
            </ul>
        </nav>
    );
}

