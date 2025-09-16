import {Link, NavLink} from "react-router-dom";
import "./Navbar.css";

export default function Navbar(){
    return (
        <nav >
            <NavLink to="/" end>Home</NavLink>
            <NavLink to="/admin" end>Admin</NavLink>
            <NavLink to="/stats" end>Stats</NavLink>
            <NavLink to="/about" end>About</NavLink>
            <NavLink to="/services" end>Services</NavLink>
        </nav>
    );
}

