import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

const ProjectList = () => {
    // Mock data for now
    const [projects, setProjects] = useState([
        { id: 1, name: 'My E-commerce Site', domain: 'shop.example.com', apiKey: 'pc_live_8f7d9a2b3c4e5' },
        { id: 2, name: 'Personal Blog', domain: 'blog.example.com', apiKey: 'pc_live_1a2b3c4d5e6f7' }
    ]);

    return (
        <div className="project-list-container">
            <div className="dashboard-page-header">
                <h2 className="header-title">Projects</h2>
                <Link to="/dashboard/projects/new" className="btn btn-primary">
                    + New Project
                </Link>
            </div>

            {projects.length === 0 ? (
                <div className="card empty-state">
                    <p>You haven't created any projects yet.</p>
                </div>
            ) : (
                <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
                        <thead style={{ background: '#f8f9fa', borderBottom: '1px solid #e0e0e0' }}>
                            <tr>
                                <th style={{ padding: '1rem' }}>Name</th>
                                <th style={{ padding: '1rem' }}>Domain</th>
                                <th style={{ padding: '1rem' }}>API Key</th>
                                <th style={{ padding: '1rem' }}>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {projects.map(project => (
                                <tr key={project.id} style={{ borderBottom: '1px solid #f0f0f0' }}>
                                    <td style={{ padding: '1rem', fontWeight: 500 }}>{project.name}</td>
                                    <td style={{ padding: '1rem', color: '#666' }}>{project.domain}</td>
                                    <td style={{ padding: '1rem', fontFamily: 'monospace', color: '#764ba2' }}>
                                        {project.apiKey}
                                    </td>
                                    <td style={{ padding: '1rem' }}>
                                        <span className="status-indicator">
                                            <span className="dot"></span> Active
                                        </span>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default ProjectList;
