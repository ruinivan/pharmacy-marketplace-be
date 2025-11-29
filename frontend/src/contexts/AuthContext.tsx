import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import api from '../services/api';

interface User {
  id?: number;
  publicId?: string;
  email: string;
  roles: string[];
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (email: string, password: string) => Promise<User>;
  register: (email: string, password: string, fullName: string, phoneNumber: string) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'));

  useEffect(() => {
    if (token) {
      const userStr = localStorage.getItem('user');
      if (userStr) {
        setUser(JSON.parse(userStr));
      }
    }
  }, [token]);

  const login = async (email: string, password: string): Promise<User> => {
    try {
      const response = await api.post('/auth/login', { email, password });
      const newToken = response.data.jwtToken || response.data.token;
      
      if (!newToken) {
        throw new Error('Token não recebido do servidor');
      }
      
      // Salva o token primeiro
      setToken(newToken);
      localStorage.setItem('token', newToken);
      
      // Configura o token no header para a próxima requisição
      api.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
      
      // Busca os dados do usuário
      const userResponse = await api.get(`/users/email/${encodeURIComponent(email)}`);
      const userData = userResponse.data;
      
      const user: User = {
        publicId: userData.publicId,
        email: userData.email,
        roles: userData.roles || []
      };
      
      setUser(user);
      localStorage.setItem('user', JSON.stringify(user));
      return user;
    } catch (error: any) {
      console.error('Erro no login:', error);
      // Remove o token se houver erro
      setToken(null);
      localStorage.removeItem('token');
      if (error.response) {
        throw error;
      }
      throw new Error('Erro de conexão com o servidor');
    }
  };

  const register = async (email: string, password: string, fullName: string, phoneNumber: string) => {
    try {
      const response = await api.post('/auth/register', {
        email,
        password,
        fullName,
        phoneNumber,
      });
      const newToken = response.data.jwtToken || response.data.token;
      
      if (!newToken) {
        throw new Error('Token não recebido do servidor');
      }
      
      setToken(newToken);
      localStorage.setItem('token', newToken);
      api.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
      
      const userResponse = await api.get(`/users/email/${encodeURIComponent(email)}`);
      const userData = userResponse.data;
      
      const user: User = {
        publicId: userData.publicId,
        email: userData.email,
        roles: userData.roles || []
      };
      
      setUser(user);
      localStorage.setItem('user', JSON.stringify(user));
    } catch (error: any) {
      console.error('Erro no registro:', error);
      setToken(null);
      localStorage.removeItem('token');
      if (error.response) {
        throw error;
      }
      throw new Error('Erro de conexão com o servidor');
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        login,
        register,
        logout,
        isAuthenticated: !!token,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

