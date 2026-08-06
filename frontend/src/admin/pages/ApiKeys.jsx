import { useEffect, useState } from "react";
import "../admin.css";

const API_BASE = import.meta.env.VITE_API_URL;

export default function ApiKeys() {
  const [keys, setKeys]           = useState([]);
  const [companyName, setCompanyName] = useState("");
  const [loading, setLoading]     = useState(true);
  const [toast, setToast]         = useState({ msg: "", type: "success" });

  const showToast = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast({ msg: "", type: "success" }), 3000);
  };

  const loadKeys = async () => {
    const token = localStorage.getItem("adminToken");
    try {
      const res  = await fetch(`${API_BASE}/api/v1/api-keys`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setKeys(await res.json());
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadKeys(); }, []);

  const issueKey = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("adminToken");
    const res = await fetch(`${API_BASE}/api/v1/api-keys`, {
      method: "POST",
      headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
      body: JSON.stringify({ companyName }),
    });
    if (res.ok) {
      setCompanyName("");
      showToast("API key issued successfully.");
      loadKeys();
    } else {
      showToast("Failed to issue API key.", "error");
    }
  };

  const copyKey = async (key) => {
    await navigator.clipboard.writeText(key);
    showToast("API key copied to clipboard.");
  };

  const updateKey = async (id, action) => {
    const token = localStorage.getItem("adminToken");
    const res = await fetch(`${API_BASE}/api/v1/api-keys/${id}/${action}`, {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    });
    if (res.ok) {
      showToast(action === "revoke" ? "API key revoked." : "API key regenerated.");
      loadKeys();
    }
  };

  const statusBadge = (status) => {
    const cls = { ACTIVE: "badge-active", REVOKED: "badge-revoked" };
    return <span className={`badge ${cls[status] ?? "badge-pending"}`}>{status}</span>;
  };

  return (
    <>
      <div className="page-header">
        <h1>API Keys</h1>
        <p>Generate and manage API access credentials for approved companies.</p>
      </div>

      {/* Issue Key Form */}
      <div className="card" style={{ marginBottom: 24 }}>
        <div className="card-header">
          <h2 className="card-title">🔑 Issue New API Key</h2>
        </div>
        <div className="card-body">
          {toast.msg && (
            <div className={`admin-toast admin-toast-${toast.type}`} style={{ marginBottom: 16 }}>
              {toast.type === "success" ? "✅" : "⚠️"} {toast.msg}
            </div>
          )}
          <form
            onSubmit={issueKey}
            style={{ display: "flex", gap: 10, alignItems: "flex-end" }}
          >
            <div className="form-group" style={{ flex: 1, margin: 0 }}>
              <label>Company Name</label>
              <input
                className="form-control"
                value={companyName}
                onChange={(e) => setCompanyName(e.target.value)}
                placeholder="e.g. Acme Corp"
                required
              />
            </div>
            <button type="submit" className="btn-admin btn-admin-primary">
              + Issue Key
            </button>
          </form>
        </div>
      </div>

      {/* Keys Table */}
      <div className="card">
        <div className="card-header">
          <h2 className="card-title">All API Keys</h2>
          <span style={{ fontSize: "0.8rem", color: "var(--text-muted)" }}>
            {keys.length} key{keys.length !== 1 ? "s" : ""}
          </span>
        </div>
        <div className="admin-table-wrap">
          {loading ? (
            <div className="loading-text">Loading keys…</div>
          ) : keys.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon">🔐</div>
              <h3>No API keys yet</h3>
              <p>Issue your first key above to get started.</p>
            </div>
          ) : (
            <table className="admin-table">
              <thead>
                <tr>
                  <th>Company</th>
                  <th>API Key</th>
                  <th>Status</th>
                  <th>Last Used</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {keys.map((key) => (
                  <tr key={key.id}>
                    <td style={{ fontWeight: 500 }}>{key.companyName}</td>
                    <td>
                      <span className="mono">{key.apiKey}</span>
                    </td>
                    <td>{statusBadge(key.status)}</td>
                    <td style={{ color: "var(--text-secondary)" }}>
                      {key.lastUsed ? new Date(key.lastUsed).toLocaleString() : "Never"}
                    </td>
                    <td>
                      <div style={{ display: "flex", gap: 6, flexWrap: "wrap" }}>
                        <button
                          className="btn-admin btn-admin-info"
                          onClick={() => copyKey(key.apiKey)}
                        >
                          📋 Copy
                        </button>
                        <button
                          className="btn-admin btn-admin-ghost"
                          onClick={() => updateKey(key.id, "regenerate")}
                        >
                          🔄 Regen
                        </button>
                        <button
                          className="btn-admin btn-admin-danger"
                          onClick={() => updateKey(key.id, "revoke")}
                        >
                          🚫 Revoke
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
