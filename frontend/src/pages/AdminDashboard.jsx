import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function AdminDashboard() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const loadRequests = async () => {
    const token = localStorage.getItem("adminToken");
    if (!token) {
      navigate("/admin/login");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/v1/contact-requests", {
        headers: { Authorization: `Bearer ${token}` }
      });
      if (!response.ok) {
        throw new Error("Unauthorized");
      }
      const data = await response.json();
      setRequests(data);
    } catch (error) {
      console.error(error);
      navigate("/admin/login");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadRequests();
  }, [navigate]);

  const updateStatus = async (id, action) => {
    const token = localStorage.getItem("adminToken");
    if (!token) {
      navigate("/admin/login");
      return;
    }

    try {
      await fetch(`http://localhost:8080/api/v1/contact-requests/${id}/${action}`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` }
      });
      loadRequests();
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div style={{ maxWidth: 1200, margin: "2rem auto", padding: "1rem" }}>
      <h1>Admin Dashboard</h1>
      <p>Review developer access requests and approve or reject them.</p>
      {loading ? (
        <p>Loading requests...</p>
      ) : (
        <table style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr>
              <th style={{ borderBottom: "1px solid #ccc", padding: "8px", textAlign: "left" }}>Name</th>
              <th style={{ borderBottom: "1px solid #ccc", padding: "8px", textAlign: "left" }}>Company</th>
              <th style={{ borderBottom: "1px solid #ccc", padding: "8px", textAlign: "left" }}>Status</th>
              <th style={{ borderBottom: "1px solid #ccc", padding: "8px", textAlign: "left" }}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {requests.map((request) => (
              <tr key={request.id}>
                <td style={{ padding: "8px" }}>{request.fullName}</td>
                <td style={{ padding: "8px" }}>{request.company || "—"}</td>
                <td style={{ padding: "8px" }}>{request.status}</td>
                <td style={{ padding: "8px" }}>
                  <button onClick={() => updateStatus(request.id, "approve")}>Approve</button>
                  <button onClick={() => updateStatus(request.id, "reject")} style={{ marginLeft: "0.5rem" }}>Reject</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
