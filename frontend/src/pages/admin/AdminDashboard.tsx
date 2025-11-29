import React from 'react';
import { Routes, Route, Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import AdminUsers from '../../components/admin/AdminUsers';
import AdminPharmacies from '../../components/admin/AdminPharmacies';
import AdminProducts from '../../components/admin/AdminProducts';
import AdminOrders from '../../components/admin/AdminOrders';
import AdminCategories from '../../components/admin/AdminCategories';
import AdminBrands from '../../components/admin/AdminBrands';
import AdminManufacturers from '../../components/admin/AdminManufacturers';
import './AdminDashboard.css';

const AdminDashboard: React.FC = () => {
  const { logout, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path: string) => {
    return location.pathname === path || location.pathname.startsWith(path + '/');
  };

  return (
    <div className="admin-dashboard">
      <header className="dashboard-header admin-header">
        <div className="header-left">
          <h1>👑 Painel SuperAdmin</h1>
          <span className="user-email">{user?.email}</span>
        </div>
        <nav>
          <Link to="/admin/users" className={isActive('/admin/users') ? 'active' : ''}>
            👥 Usuários
          </Link>
          <Link to="/admin/pharmacies" className={isActive('/admin/pharmacies') ? 'active' : ''}>
            🏥 Farmácias
          </Link>
          <Link to="/admin/products" className={isActive('/admin/products') ? 'active' : ''}>
            💊 Produtos
          </Link>
          <Link to="/admin/orders" className={isActive('/admin/orders') ? 'active' : ''}>
            📦 Pedidos
          </Link>
          <Link to="/admin/categories" className={isActive('/admin/categories') ? 'active' : ''}>
            📂 Categorias
          </Link>
          <Link to="/admin/brands" className={isActive('/admin/brands') ? 'active' : ''}>
            🏷️ Marcas
          </Link>
          <Link to="/admin/manufacturers" className={isActive('/admin/manufacturers') ? 'active' : ''}>
            🏭 Fabricantes
          </Link>
          <button onClick={() => { logout(); navigate('/'); }} className="btn btn-secondary">
            🚪 Sair
          </button>
        </nav>
      </header>
      <main className="dashboard-main">
        <Routes>
          <Route path="users" element={<AdminUsers />} />
          <Route path="pharmacies" element={<AdminPharmacies />} />
          <Route path="products" element={<AdminProducts />} />
          <Route path="orders" element={<AdminOrders />} />
          <Route path="categories" element={<AdminCategories />} />
          <Route path="brands" element={<AdminBrands />} />
          <Route path="manufacturers" element={<AdminManufacturers />} />
          <Route path="*" element={<AdminUsers />} />
        </Routes>
      </main>
    </div>
  );
};

export default AdminDashboard;

