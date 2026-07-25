import "../admin.css";

export default function Requests() {
  return (
    <>
      <div className="page-header">
        <h1>API Requests</h1>
        <p>View and audit all incoming PassiveCaptcha API calls from integrated clients.</p>
      </div>
      <div className="card">
        <div className="card-body">
          <div className="coming-soon">
            <div className="coming-soon-icon">📋</div>
            <h2>API Request Log Coming Soon</h2>
            <p>A real-time log of all API calls, response codes, and latency will appear here.</p>
          </div>
        </div>
      </div>
    </>
  );
}
