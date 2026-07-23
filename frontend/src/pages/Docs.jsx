import React from 'react';
import './Dashboard.css';

const Docs = () => {
  return (
    <div className="dashboard-content" style={{ marginLeft: 0 }}>
      {/* Reusing dashboard content style but might be standalone page or inside dashboard */}
      <h1 className="header-title">Integration Documentation</h1>
      
      <div className="card">
        <h3>Step 1: Include the SDK</h3>
        <p>Add the following script tag to your website's <code>&lt;head&gt;</code> or just before the closing <code>&lt;/body&gt;</code> tag.</p>
        <pre style={{ background: '#f4f4f4', padding: '1rem', borderRadius: '8px', overflowX: 'auto' }}>
          <code>&lt;script src="https://passivecaptcha.com/sdk.js"&gt;&lt;/script&gt;</code>
        </pre>
      </div>

      <div className="card">
        <h3>Step 2: Initialize</h3>
        <p>Initialize the library with your API Key (found in your Dashboard).</p>
        <pre style={{ background: '#f4f4f4', padding: '1rem', borderRadius: '8px', overflowX: 'auto' }}>
          <code>
{`try {
  PassiveCaptcha.init("YOUR_API_KEY");
} catch (e) {
  console.error("Failed to init passive captcha", e);
}`}
          </code>
        </pre>
      </div>

      <div className="card">
        <h3>Step 3: Verify Actions</h3>
        <p>Call the <code>verify()</code> method before sensitive actions (e.g., form submission).</p>
        <pre style={{ background: '#f4f4f4', padding: '1rem', borderRadius: '8px', overflowX: 'auto' }}>
          <code>
{`document.getElementById('myForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  
  try {
    const result = await PassiveCaptcha.verify();
    
    // Check specific status
    if (result.status === 'human') {
      // Proceed with submission
      console.log("Human verified! Score:", result.score);
      e.target.submit();
    } else {
      // Show challenge or block
      console.log("Bot detected or suspicious behavior");
      alert("Verification failed: " + result.status);
    }
  } catch (error) {
    // Handle error (fail open or closed depending on security needs)
  }
});`}
          </code>
        </pre>
      </div>
    </div>
  );
};

export default Docs;
