import "./Admin.css";

export default function Admin() {
  return (
    <div className="admin-container">
      <div className="admin-content">
        <h1>Admin Dashboard</h1>
        <p>
          Welcome to the Passive Captcha admin panel. Monitor and manage your 
          security settings and analytics from here.
        </p>

        <div className="admin-grid">
          <div className="admin-card">
            <h2>Security Settings</h2>
            <p>Configure bot detection sensitivity and security rules.</p>
            <button className="admin-btn">Configure</button>
          </div>

          <div className="admin-card">
            <h2>Analytics</h2>
            <p>View detailed analytics and threat reports.</p>
            <button className="admin-btn">View Reports</button>
          </div>

          <div className="admin-card">
            <h2>User Management</h2>
            <p>Manage user access and permissions.</p>
            <button className="admin-btn">Manage Users</button>
          </div>

          <div className="admin-card">
            <h2>API Keys</h2>
            <p>Generate and manage your API keys.</p>
            <button className="admin-btn">Manage API</button>
          </div>
        </div>
      </div>
    </div>
  );
}