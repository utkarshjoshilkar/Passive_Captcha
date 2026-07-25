import { useLocation } from "react-router-dom";
import "../admin.css";

const PAGE_TITLES = {
  "/admin/dashboard":           "Overview",
  "/admin/dashboard/requests":  "API Requests",
  "/admin/dashboard/keys":      "API Keys",
  "/admin/dashboard/sessions":  "Sessions",
  "/admin/dashboard/analytics": "Analytics",
  "/admin/dashboard/models":    "ML Models",
  "/admin/dashboard/settings":  "Settings",
};

export default function Topbar() {
  const location = useLocation();
  const pageTitle = PAGE_TITLES[location.pathname] ?? "Admin";

  return (
    <header className="admin-topbar">
      <div className="topbar-left">
        <span className="topbar-breadcrumb">Admin Portal</span>
        <span style={{ color: "#cbd5e1" }}>/</span>
        <span className="topbar-page-title">{pageTitle}</span>
      </div>

      <div className="topbar-right">
        <span style={{ fontSize: "0.82rem", color: "var(--text-secondary)" }}>
          Administrator
        </span>
        <div className="topbar-avatar" title="Admin">A</div>
      </div>
    </header>
  );
}
