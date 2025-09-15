import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Admin from "./pages/Admin";
import Stats from "./pages/Stats";
import Navbar from "./components/Navbar";

export default function App() {
  return (
    <div>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/stats" element={<Stats />} />
      </Routes>
    </div>
  );
}
