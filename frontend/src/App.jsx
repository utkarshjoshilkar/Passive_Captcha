import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Contact from "./pages/Contact";
import Demo from "./pages/Demo";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import AdminLayout from "./admin/layouts/AdminLayout";
import AdminLogin from "./admin/pages/Login";
import AdminDashboard from "./admin/pages/Dashboard";
import Requests from "./admin/pages/Requests";
import ApiKeys from "./admin/pages/ApiKeys";
import Sessions from "./admin/pages/Sessions";
import Analytics from "./admin/pages/Analytics";
import Models from "./admin/pages/Models";
import Settings from "./admin/pages/Settings";
import "./App.css";

export default function App() {
  return (
    <div className="app">
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/demo" element={<Demo />} />
          <Route path="/admin/login" element={<AdminLogin />} />
          <Route element={<AdminLayout />}>
            <Route path="/admin/dashboard" element={<AdminDashboard />} />
            <Route path="/admin/dashboard/requests" element={<Requests />} />
            <Route path="/admin/dashboard/keys" element={<ApiKeys />} />
            <Route path="/admin/dashboard/sessions" element={<Sessions />} />
            <Route path="/admin/dashboard/analytics" element={<Analytics />} />
            <Route path="/admin/dashboard/models" element={<Models />} />
            <Route path="/admin/dashboard/settings" element={<Settings />} />
          </Route>
        </Routes>
      </main>
      <Footer />
    </div>
  );
}