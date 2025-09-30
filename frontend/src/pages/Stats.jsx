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
        if (!response.ok) throw new Error("Failed to fetch stats");
        const data = await response.json();
        setStats(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
    // Refresh stats every 10 seconds
    const interval = setInterval(fetchStats, 10000);
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return (
      <div className="stats-container">
        <div className="stats-content">
          <h1>Passive Captcha Stats</h1>
          <div className="loading-spinner"></div>
          <p>Loading statistics...</p>
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
            <strong>Error:</strong> {error}
            <p>Make sure the backend server is running on http://localhost:8080</p>
            <button onClick={() => window.location.reload()} className="retry-btn">
              Retry
            </button>
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
        
        {stats?.totalRequests === 0 && (
          <div className="no-data-message">
            <p>No data yet. Try the <a href="/demo">demo</a> to generate some statistics!</p>
          </div>
        )}
      </div>
    </div>
  );
}