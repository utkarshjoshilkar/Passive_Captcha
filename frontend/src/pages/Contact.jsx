import { useState } from "react";
import "./Contact.css";

export default function Contact() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    company: "",
    message: ""
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Create FormData object and append all fields
    const web3FormData = new FormData();
    web3FormData.append("access_key", "d4992ade-408a-4e1e-b7ef-4cb9fbe5afac"); // Replace with your actual key
    web3FormData.append("name", formData.name);
    web3FormData.append("email", formData.email);
    web3FormData.append("company", formData.company);
    web3FormData.append("message", formData.message);

    try {
      const response = await fetch("https://api.web3forms.com/submit", {
        method: "POST",
        body: web3FormData
      });

      const data = await response.json();

      if (data.success) {
        alert("Thank you! We'll contact you within 24 hours.");
        setFormData({ name: "", email: "", company: "", message: "" });
      } else {
        console.log("Error", data);
        alert("Something went wrong. Please try again.");
      }
    } catch (error) {
      console.log("Error", error);
      alert("Network error. Please check your connection and try again.");
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
              
              <div className="contact-method">
                <div className="method-icon">💬</div>
                <div>
                  <h3>Live Chat</h3>
                  <p>Available during business hours</p>
                </div>
              </div>
            </div>
          </div>

          <form className="contact-form" onSubmit={handleSubmit}>
            <h2>Send us a Message</h2>
            
            <div className="form-group">
              <label>Full Name *</label>
              <input
                type="text"
                name="name"
                value={formData.name}
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

            <button type="submit" className="submit-btn">
              Send Message
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}