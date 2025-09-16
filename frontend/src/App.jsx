import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Admin from "./pages/Admin";
import Stats from "./pages/Stats";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import About from "./pages/About";

export default function App() {
  return (
    <div>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/stats" element={<Stats />} />
        <Route path="/about" element={<About/>}/>
      </Routes>
      <Footer/>
    </div>
  );
}
