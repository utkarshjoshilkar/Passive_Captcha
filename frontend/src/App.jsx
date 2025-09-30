import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Admin from "./pages/Admin";
import Stats from "./pages/Stats";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import About from "./pages/About";
import Services from "./pages/Services";
import PassiveCaptchaDemo from "./components/PassiveCaptchaDemo";
import ProtectedLogin from "./components/ProtectedLogin"; // Add this line
import "./App.css";

export default function App() {
  return (
    <div className="app">
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/admin" element={<Admin />} />
          <Route path="/stats" element={<Stats />} />
          <Route path="/about" element={<About />} />
          <Route path="/services" element={<Services />} />
          <Route path="/demo" element={<PassiveCaptchaDemo />} />
          <Route path="/login" element={<ProtectedLogin />} /> {/* Add this line */}
        </Routes>
      </main>
      <Footer />
    </div>
  );
}