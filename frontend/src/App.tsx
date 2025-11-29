import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { ToastProvider } from './contexts/ToastContext';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import CustomerDashboard from './pages/customer/CustomerDashboard';
import PharmacyDashboard from './pages/pharmacy/PharmacyDashboard';
import DeliveryDashboard from './pages/delivery/DeliveryDashboard';
import AdminDashboard from './pages/admin/AdminDashboard';
import ProtectedRoute from './components/ProtectedRoute';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <ToastProvider>
        <Router>
          <div className="App">
            <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/admin/login" element={<LoginPage isAdmin={true} />} />
            <Route
              path="/customer/*"
              element={
                <ProtectedRoute requiredRole="ROLE_CUSTOMER">
                  <CustomerDashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/pharmacy/*"
              element={
                <ProtectedRoute requiredRole="ROLE_PHARMACY_ADMIN">
                  <PharmacyDashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/delivery/*"
              element={
                <ProtectedRoute requiredRole="ROLE_DELIVERY_PERSONNEL">
                  <DeliveryDashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/*"
              element={
                <ProtectedRoute requiredRole="ROLE_ADMIN">
                  <AdminDashboard />
                </ProtectedRoute>
              }
            />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </div>
      </Router>
      </ToastProvider>
    </AuthProvider>
  );
}

export default App;

