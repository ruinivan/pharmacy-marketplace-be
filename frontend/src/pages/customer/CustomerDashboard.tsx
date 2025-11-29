import React from 'react';
import { Routes, Route, Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import ProductList from '../../components/customer/ProductList';
import Cart from '../../components/customer/Cart';
import Orders from '../../components/customer/Orders';
import Favorites from '../../components/customer/Favorites';
import Notifications from '../../components/customer/Notifications';
import './CustomerDashboard.css';

const CustomerDashboard: React.FC = () => {
  const { logout, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path: string) => {
    return location.pathname === path || location.pathname.startsWith(path + '/');
  };

  return (
    <div className="customer-dashboard">
      <header className="dashboard-header">
        <div className="header-left">
          <h1>💊 Pharmacy Marketplace</h1>
          <span className="user-email">{user?.email}</span>
        </div>
        <nav>
          <Link to="/customer/products" className={isActive('/customer/products') ? 'active' : ''}>
            🛍️ Produtos
          </Link>
          <Link to="/customer/favorites" className={isActive('/customer/favorites') ? 'active' : ''}>
            ❤️ Favoritos
          </Link>
          <Link to="/customer/cart" className={isActive('/customer/cart') ? 'active' : ''}>
            🛒 Carrinho
          </Link>
          <Link to="/customer/orders" className={isActive('/customer/orders') ? 'active' : ''}>
            📦 Pedidos
          </Link>
          <Link to="/customer/notifications" className={isActive('/customer/notifications') ? 'active' : ''}>
            🔔 Notificações
          </Link>
          <button onClick={() => { logout(); navigate('/'); }} className="btn btn-secondary">
            🚪 Sair
          </button>
        </nav>
      </header>
      <main className="dashboard-main">
        <Routes>
          <Route path="products" element={<ProductList />} />
          <Route path="favorites" element={<Favorites />} />
          <Route path="cart" element={<Cart />} />
          <Route path="orders" element={<Orders />} />
          <Route path="notifications" element={<Notifications />} />
          <Route path="*" element={<ProductList />} />
        </Routes>
      </main>
    </div>
  );
};

export default CustomerDashboard;

