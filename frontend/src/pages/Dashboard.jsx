import React from 'react';
import { Routes, Route, Link, Outlet, useLocation } from 'react-router-dom';
import ProjectList from '../components/dashboard/ProjectList';
import CreateProject from '../components/dashboard/CreateProject';
import './Dashboard.css';

const Dashboard = () => {
    const location = useLocation();

    return (
        <div className="dashboard-layout">
            <aside className="dashboard-sidebar">
                <div className="sidebar-header">
                    <h3>Developer Console</h3>
                </div>
                <nav className="sidebar-nav">
                    <Link to="/dashboard" className={`nav-item ${location.pathname === '/dashboard' ? 'active' : ''}`}>
                        Overview
                    </Link>
                    <Link to="/dashboard/projects/new" className={`nav-item ${location.pathname === '/dashboard/projects/new' ? 'active' : ''}`}>
                        New Project
                    </Link>
                    <Link to="/docs" className="nav-item">
                        Documentation
                    </Link>
                    <Link to="/settings" className="nav-item">
                        Settings
                    </Link>
                </nav>
            </aside>
            <main className="dashboard-content">
                <Routes>
                    <Route path="/" element={<ProjectList />} />
                    <Route path="/projects/new" element={<CreateProject />} />
                </Routes>
                {/* <Outlet /> if we wanted to use nested from App.jsx, but we are doing Routes here */}
            </main>
        </div>
    );
};

export default Dashboard;
