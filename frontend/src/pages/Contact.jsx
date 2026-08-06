import { useState } from "react";
import "./Contact.css";

const API_BASE = import.meta.env.VITE_API_URL;

export default function Contact() {
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    company: "",
    message: ""
  });
  const [statusMessage, setStatusMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(`${API_BASE}/api/v1/contact`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          fullName: formData.fullName,
          email: formData.email,
          company: formData.company,
          message: formData.message
        })
      });

      const data = await response.json();

      if (response.ok) {
        setStatusMessage(data.message || "Your request has been recorded successfully.");
        setFormData({ fullName: "", email: "", company: "", message: "" });
      } else {
        throw new Error(data.message || "Something went wrong. Please try again.");
      }
    } catch (error) {
      console.log("Error", error);
      setStatusMessage(error.message || "Network error. Please check your connection and try again.");
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div className="contact-page">
      <div className="contact-container">
        <div className="contact-content">
          <div className="contact-info">
            <h1>Contact Us</h1>
            <p>Get in touch to learn more about PassiveCaptcha and how it can protect your applications.</p>
            
            <div className="contact-methods">
              <div className="contact-method">
                <div className="method-icon">📧</div>
                <div>
                  <h3>Email Us</h3>
                  <p>contact@passivecaptcha.com</p>
                </div>
              </div>
              
              {/* <div className="contact-method">
                <div className="method-icon">💬</div>
                <div>
                  <h3>Live Chat</h3>
                  <p>Available during business hours</p>
                </div>
              </div> */}
            </div>
          </div>

          <form className="contact-form" onSubmit={handleSubmit}>
            <h2>Request Developer Access</h2>
            
            <div className="form-group">
              <label>Full Name *</label>
              <input
                type="text"
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
                required
              />
            </div>
            
            <div className="form-group">
              <label>Email *</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label>Company</label>
              <input
                type="text"
                name="company"
                value={formData.company}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>Message *</label>
              <textarea
                name="message"
                value={formData.message}
                onChange={handleChange}
                rows="4"
                placeholder="Tell us about your project or how we can help..."
                required
              />
            </div>

            {statusMessage && (
              <p className="form-status" style={{ marginTop: "12px", color: "#0f766e" }}>
                {statusMessage}
              </p>
            )}

            <button type="submit" className="submit-btn">
              Submit Request
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}