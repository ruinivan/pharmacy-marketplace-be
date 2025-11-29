import React from 'react';
import { Routes, Route, Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import DeliveryList from '../../components/delivery/DeliveryList';
import './DeliveryDashboard.css';

const DeliveryDashboard: React.FC = () => {
  const { logout, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path: string) => {
    return location.pathname === path || location.pathname.startsWith(path + '/');
  };

  return (
    <div className="delivery-dashboard">
      <header className="dashboard-header">
        <div className="header-left">
          <h1>🚚 Painel do Entregador</h1>
          <span className="user-email">{user?.email}</span>
        </div>
        <nav>
          <Link to="/delivery/deliveries" className={isActive('/delivery/deliveries') ? 'active' : ''}>
            📦 Entregas
          </Link>
          <button onClick={() => { logout(); navigate('/'); }} className="btn btn-secondary">
            🚪 Sair
          </button>
        </nav>
      </header>
      <main className="dashboard-main">
        <Routes>
          <Route path="deliveries" element={<DeliveryList />} />
          <Route path="*" element={<DeliveryList />} />
        </Routes>
      </main>
    </div>
  );
};

export default DeliveryDashboard;

