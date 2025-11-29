import React, { useState, useEffect } from 'react';
import { useNavigate, Link, useSearchParams } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './LoginPage.css';

interface LoginPageProps {
  isAdmin?: boolean;
}

const LoginPage: React.FC<LoginPageProps> = ({ isAdmin = false }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [searchParams] = useSearchParams();
  const userType = searchParams.get('type') || (isAdmin ? 'admin' : 'customer');
  const { login } = useAuth();
  const navigate = useNavigate();

  const getRedirectPath = (roles: string[]): string => {
    if (roles.includes('ROLE_ADMIN')) {
      return '/admin';
    } else if (roles.includes('ROLE_PHARMACY_ADMIN')) {
      return '/pharmacy';
    } else if (roles.includes('ROLE_DELIVERY_PERSONNEL')) {
      return '/delivery';
    } else if (roles.includes('ROLE_CUSTOMER')) {
      return '/customer';
    }
    return '/';
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    try {
      const user = await login(email, password);
      const redirectPath = getRedirectPath(user.roles);
      navigate(redirectPath);
    } catch (err: any) {
      console.error('Erro no login:', err);
      const errorMessage = err.response?.data?.message 
        || err.response?.data?.error 
        || err.message 
        || 'Erro ao fazer login. Verifique suas credenciais.';
      setError(errorMessage);
    }
  };

  const getTitle = () => {
    if (isAdmin) return 'Login SuperAdmin';
    switch (userType) {
      case 'pharmacy': return 'Login Farmácia';
      case 'delivery': return 'Login Entregador';
      case 'customer': return 'Login Cliente';
      default: return 'Login';
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <h2>{getTitle()}</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Senha</label>
            <div className="password-input-wrapper">
              <input
                type={showPassword ? "text" : "password"}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                aria-label={showPassword ? "Ocultar senha" : "Mostrar senha"}
              >
                {showPassword ? "Ocultar" : "Mostrar"}
              </button>
            </div>
          </div>
          {error && <div className="error">{error}</div>}
          <button type="submit" className="btn btn-primary btn-block">
            Entrar
          </button>
        </form>
        {!isAdmin && (
          <p className="register-link">
            Não tem conta? <Link to="/register">Cadastre-se</Link>
          </p>
        )}
        <Link to="/" className="back-link">Voltar</Link>
      </div>
    </div>
  );
};

export default LoginPage;

