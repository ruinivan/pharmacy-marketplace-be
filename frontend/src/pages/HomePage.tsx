import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './HomePage.css';

const HomePage: React.FC = () => {
  const { isAuthenticated, user, logout } = useAuth();

  return (
    <div className="home-page">
      <header className="home-header">
        <h1>Pharmacy Marketplace</h1>
        {isAuthenticated ? (
          <div className="user-menu">
            <span>Olá, {user?.email}</span>
            <button onClick={logout} className="btn btn-secondary">Sair</button>
          </div>
        ) : (
          <Link to="/login" className="btn btn-primary">Entrar</Link>
        )}
      </header>

      <main className="home-main">
        <h2>Escolha seu tipo de usuário</h2>
        <div className="user-type-cards">
          <Link to="/login?type=customer" className="user-type-card">
            <h3>Cliente</h3>
            <p>Compre produtos farmacêuticos</p>
          </Link>
          <Link to="/login?type=pharmacy" className="user-type-card">
            <h3>Farmácia</h3>
            <p>Gerencie produtos e pedidos</p>
          </Link>
          <Link to="/login?type=delivery" className="user-type-card">
            <h3>Entregador</h3>
            <p>Gerencie entregas</p>
          </Link>
          <Link to="/admin/login" className="user-type-card admin-card">
            <h3>SuperAdmin</h3>
            <p>Painel administrativo completo</p>
          </Link>
        </div>
      </main>
    </div>
  );
};

export default HomePage;

