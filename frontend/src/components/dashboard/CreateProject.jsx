import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const CreateProject = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: '',
        domain: ''
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Creating project:', formData);
        // Simulate API call
        setTimeout(() => {
            navigate('/dashboard');
        }, 500);
    };

    return (
        <div className="create-project-container">
            <div className="dashboard-page-header">
                <h2 className="header-title">New Project</h2>
            </div>

            <div className="card" style={{ maxWidth: '600px' }}>
                <form onSubmit={handleSubmit} className="auth-form">
                    <div className="form-group">
                        <label className="form-label">Project Name</label>
                        <input 
                            type="text" 
                            name="name"
                            className="form-input" 
                            value={formData.name}
                            onChange={handleChange}
                            placeholder="e.g. My Awesome Shop"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label className="form-label">Domain</label>
                        <input 
                            type="text" 
                            name="domain"
                            className="form-input" 
                            value={formData.domain}
                            onChange={handleChange}
                            placeholder="e.g. example.com"
                            required
                        />
                        <small style={{ color: '#888', marginTop: '0.25rem', display: 'block' }}>
                            Requests will only be accepted from this domain.
                        </small>
                    </div>

                    <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
                        <button type="submit" className="btn btn-primary">Create Project</button>
                        <button type="button" onClick={() => navigate('/dashboard')} className="btn btn-secondary">Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CreateProject;
