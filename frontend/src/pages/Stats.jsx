import { useEffect, useState } from "react";
import "./Stats.css";

export default function Stats() {
  const [stats, setStats] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        const response = await fetch("http://localhost:8080/api/v1/stats");
        if (!response.ok) throw new Error("Network response was not ok");
        const data = await response.json();
        setStats(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  if (loading) {
    return (
      <div className="stats-container">
        <div className="stats-content">
          <h1>Passive Captcha Stats</h1>
          <div className="loading">Loading stats...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="stats-container">
        <div className="stats-content">
          <h1>Passive Captcha Stats</h1>
          <div className="error-message">
            Error: {error}
            <p>Please make sure the server is running on port 8080</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="stats-container">
      <div className="stats-content">
        <h1>Passive Captcha Stats</h1>
        <div className="stats-grid">
          <div className="stat-card">
            <h3>Total Requests</h3>
            <div className="stat-number">{stats?.totalRequests || 0}</div>
          </div>
          
          <div className="stat-card">
            <h3>Allowed Requests</h3>
            <div className="stat-number">{stats?.allowedRequests || 0}</div>
          </div>
          
          <div className="stat-card">
            <h3>Challenged Requests</h3>
            <div className="stat-number">{stats?.challengedRequests || 0}</div>
          </div>
          
          <div className="stat-card highlight">
            <h3>Allow Percentage</h3>
            <div className="stat-number">
              {stats?.allowPercentage || 0}%
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}