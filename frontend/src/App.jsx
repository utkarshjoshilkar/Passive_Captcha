import { Routes, Route } from "react-router-dom";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Dashboard from "./pages/Dashboard";
import Contact from "./pages/Contact";
import Demo from "./pages/Demo";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import "./App.css";

import Docs from "./pages/Docs";

export default function App() {
  return (
    <div className="app">
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/dashboard/*" element={<Dashboard />} />
          <Route path="/docs" element={<Docs />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/demo" element={<Demo />} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}