import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../admin.css";

const API_BASE = import.meta.env.VITE_API_URL;

export default function Dashboard() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [toast, setToast] = useState("");
  const navigate = useNavigate();

  const showToast = (msg) => {
    setToast(msg);
    setTimeout(() => setToast(""), 3000);
  };

  const loadRequests = async () => {
    const token = localStorage.getItem("adminToken");
    if (!token) { navigate("/admin/login"); return; }
    try {
      const res = await fetch(`${API_BASE}/api/v1/contact-requests`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Unauthorized");
      setRequests(await res.json());
    } catch {
      navigate("/admin/login");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadRequests(); }, []);

  const updateStatus = async (id, action) => {
    const token = localStorage.getItem("adminToken");
    if (!token) { navigate("/admin/login"); return; }
    try {
      const res = await fetch(
        `${API_BASE}/api/v1/contact-requests/${id}/${action}`,
        { method: "POST", headers: { Authorization: `Bearer ${token}` } }
      );
      if (res.ok) {
        showToast(`Request ${action}d successfully.`);
        loadRequests();
      }
    } catch (err) {
      console.error(err);
    }
  };

  const filtered = requests.filter((r) => {
    const term = search.toLowerCase();
    return `${r.fullName} ${r.company ?? ""} ${r.email ?? ""}`.toLowerCase().includes(term);
  });

  const pending  = requests.filter((r) => r.status === "PENDING").length;
  const approved = requests.filter((r) => r.status === "APPROVED").length;
  const rejected = requests.filter((r) => r.status === "REJECTED").length;

  const statusBadge = (status) => {
    const cls = { PENDING: "badge-pending", APPROVED: "badge-approved", REJECTED: "badge-rejected" };
    return <span className={`badge ${cls[status] ?? ""}`}>{status}</span>;
  };

  return (
    <>
      <div className="page-header">
        <h1>Dashboard Overview</h1>
        <p>Review and manage developer access requests submitted via the contact form.</p>
      </div>

      {/* Stats */}
      <div className="stats-grid">
        <div className="stat-card">
          <span className="stat-card-icon">📋</span>
          <div className="stat-card-label">Total Requests</div>
          <div className="stat-card-value">{requests.length}</div>
        </div>
        <div className="stat-card">
          <span className="stat-card-icon">⏳</span>
          <div className="stat-card-label">Pending</div>
          <div className="stat-card-value warning">{pending}</div>
        </div>
        <div className="stat-card">
          <span className="stat-card-icon">✅</span>
          <div className="stat-card-label">Approved</div>
          <div className="stat-card-value success">{approved}</div>
        </div>
        <div className="stat-card">
          <span className="stat-card-icon">❌</span>
          <div className="stat-card-label">Rejected</div>
          <div className="stat-card-value danger">{rejected}</div>
        </div>
      </div>

      {/* Toast */}
      {toast && <div className="admin-toast admin-toast-success">✅ {toast}</div>}

      {/* Table Card */}
      <div className="card">
        <div className="card-header">
          <h2 className="card-title">Access Requests</h2>
          <div className="search-bar">
            <span className="search-icon">🔍</span>
            <input
              className="form-control"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search by name, company, email…"
            />
          </div>
        </div>

        <div className="admin-table-wrap">
          {loading ? (
            <div className="loading-text">Loading requests…</div>
          ) : filtered.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon">📭</div>
              <h3>No requests found</h3>
              <p>There are no requests matching your search.</p>
            </div>
          ) : (
            <table className="admin-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Company</th>
                  <th>Email</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((r) => (
                  <tr key={r.id}>
                    <td style={{ fontWeight: 500 }}>{r.fullName}</td>
                    <td>{r.company ?? "—"}</td>
                    <td style={{ color: "var(--text-secondary)" }}>{r.email ?? "—"}</td>
                    <td>{statusBadge(r.status)}</td>
                    <td>
                      <div style={{ display: "flex", gap: 6 }}>
                        <button
                          className="btn-admin btn-admin-success"
                          onClick={() => updateStatus(r.id, "approve")}
                        >
                          ✓ Approve
                        </button>
                        <button
                          className="btn-admin btn-admin-danger"
                          onClick={() => updateStatus(r.id, "reject")}
                        >
                          ✕ Reject
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </>
  );
}
