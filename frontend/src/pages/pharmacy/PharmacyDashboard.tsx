import React from 'react';
import { Routes, Route, Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import PharmacyOrders from '../../components/pharmacy/PharmacyOrders';
import PharmacyProducts from '../../components/pharmacy/PharmacyProducts';
import PharmacyInventory from '../../components/pharmacy/PharmacyInventory';
import PharmacyPromotions from '../../components/pharmacy/PharmacyPromotions';
import './PharmacyDashboard.css';

const PharmacyDashboard: React.FC = () => {
  const { logout, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path: string) => {
    return location.pathname === path || location.pathname.startsWith(path + '/');
  };

  return (
    <div className="pharmacy-dashboard">
      <header className="dashboard-header">
        <div className="header-left">
          <h1>🏥 Painel da Farmácia</h1>
          <span className="user-email">{user?.email}</span>
        </div>
        <nav>
          <Link to="/pharmacy/orders" className={isActive('/pharmacy/orders') ? 'active' : ''}>
            📦 Pedidos
          </Link>
          <Link to="/pharmacy/products" className={isActive('/pharmacy/products') ? 'active' : ''}>
            💊 Produtos
          </Link>
          <Link to="/pharmacy/inventory" className={isActive('/pharmacy/inventory') ? 'active' : ''}>
            📋 Estoque
          </Link>
          <Link to="/pharmacy/promotions" className={isActive('/pharmacy/promotions') ? 'active' : ''}>
            🎯 Promoções
          </Link>
          <button onClick={() => { logout(); navigate('/'); }} className="btn btn-secondary">
            🚪 Sair
          </button>
        </nav>
      </header>
      <main className="dashboard-main">
        <Routes>
          <Route path="orders" element={<PharmacyOrders />} />
          <Route path="products" element={<PharmacyProducts />} />
          <Route path="inventory" element={<PharmacyInventory />} />
          <Route path="promotions" element={<PharmacyPromotions />} />
          <Route path="*" element={<PharmacyOrders />} />
        </Routes>
      </main>
    </div>
  );
};

export default PharmacyDashboard;

