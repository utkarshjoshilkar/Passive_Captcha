import { useEffect , useState} from "react";

export default function Stats() {
  const [stats, setStats] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/stats")
      .then(res => {
        if (!res.ok) throw new Error("Network response was not ok");
        return res.json();
      })
      .then(data => setStats(data))
      .catch(err => setError(err.message));
  }, []);

   if (error) return <p style={{ textAlign: "center", marginTop: "50px", color: "red" }}>Error: {error}</p>;
  if (!stats) return <p style={{ textAlign: "center", marginTop: "50px" }}>Loading stats...</p>;


  return (
    <div className="stats-container">
      <h1>Passive Captcha Stats</h1>
      <ul>
        <li>Total Requests: {stats.totalRequests}</li>
        <li>Allowed Requests: {stats.allowedRequests}</li>
        <li>Challenged Requests: {stats.challengedRequests}</li>
        <li>Allow Percentage: {stats.allowPercentage}%</li>
      </ul>
    </div>
  );
}
