import { Link, useLocation, useNavigate } from "react-router-dom";
import "../admin.css";

const NAV_ITEMS = [
  { label: "Overview",     path: "/admin/dashboard",           icon: "📊" },
  { label: "API Requests", path: "/admin/dashboard/requests",  icon: "📋" },
  { label: "API Keys",     path: "/admin/dashboard/keys",      icon: "🔑" },
  { label: "Sessions",     path: "/admin/dashboard/sessions",  icon: "🕐" },
  { label: "Analytics",    path: "/admin/dashboard/analytics", icon: "📈" },
  { label: "ML Models",    path: "/admin/dashboard/models",    icon: "🧠" },
  { label: "Settings",     path: "/admin/dashboard/settings",  icon: "⚙️" },
];

export default function Sidebar() {
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("adminToken");
    navigate("/admin/login");
  };

  return (
    <aside className="admin-sidebar">
      <Link to="/" className="sidebar-logo">
        <span className="sidebar-logo-icon">🛡️</span>
        <span className="sidebar-logo-text">PassiveCaptcha</span>
        <span className="sidebar-logo-badge">Admin</span>
      </Link>

      <nav className="sidebar-nav">
        <span className="sidebar-section-label">Navigation</span>
        {NAV_ITEMS.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={`sidebar-link ${location.pathname === item.path ? "active" : ""}`}
          >
            <span className="sidebar-link-icon">{item.icon}</span>
            <span>{item.label}</span>
          </Link>
        ))}
      </nav>

      <div className="sidebar-footer">
        <button className="sidebar-logout" onClick={handleLogout}>
          <span className="sidebar-link-icon">🚪</span>
          <span>Logout</span>
        </button>
      </div>
    </aside>
  );
}
