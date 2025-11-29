import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import './AdminOrders.css';

interface Order {
  publicId: string;
  orderCode: string;
  customerId: number;
  pharmacyId: number;
  orderStatus: string;
  total: number;
  createdAt: string;
  items: any[];
}

const AdminOrders: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterStatus, setFilterStatus] = useState<string>('ALL');

  useEffect(() => {
    loadOrders();
  }, [filterStatus]);

  const loadOrders = async () => {
    try {
      setError('');
      const response = await api.get('/orders');
      let ordersData = response.data;
      
      // Filtrar por status se necessário
      if (filterStatus !== 'ALL') {
        ordersData = ordersData.filter((order: Order) => order.orderStatus === filterStatus);
      }
      
      setOrders(ordersData);
    } catch (err: any) {
      console.error('Erro ao carregar pedidos:', err);
      setError(err.response?.data?.message || 'Erro ao carregar pedidos');
    } finally {
      setLoading(false);
    }
  };

  const getStatusLabel = (status: string): string => {
    const statusMap: { [key: string]: string } = {
      'PENDING': 'Pendente',
      'AWAITING_PAYMENT': 'Aguardando Pagamento',
      'AWAITING_PRESCRIPTION': 'Aguardando Receita',
      'PROCESSING': 'Em Processamento',
      'SHIPPED': 'Enviado',
      'DELIVERED': 'Entregue',
      'CANCELLED': 'Cancelado',
      'REFUNDED': 'Reembolsado'
    };
    return statusMap[status] || status;
  };

  const getStatusClass = (status: string): string => {
    const classMap: { [key: string]: string } = {
      'PENDING': 'status-pending',
      'AWAITING_PAYMENT': 'status-warning',
      'AWAITING_PRESCRIPTION': 'status-warning',
      'PROCESSING': 'status-processing',
      'SHIPPED': 'status-shipped',
      'DELIVERED': 'status-delivered',
      'CANCELLED': 'status-cancelled',
      'REFUNDED': 'status-refunded'
    };
    return classMap[status] || '';
  };

  if (loading) {
    return (
      <div className="admin-orders">
        <LoadingSpinner message="Carregando pedidos..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="admin-orders">
        <ErrorMessage message={error} onRetry={loadOrders} />
      </div>
    );
  }

  return (
    <div className="admin-orders">
      <h2>Gerenciamento de Pedidos</h2>
      
      <div className="filter-section">
        <label>Filtrar por Status:</label>
        <select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
          <option value="ALL">Todos</option>
          <option value="PENDING">Pendente</option>
          <option value="AWAITING_PAYMENT">Aguardando Pagamento</option>
          <option value="AWAITING_PRESCRIPTION">Aguardando Receita</option>
          <option value="PROCESSING">Em Processamento</option>
          <option value="SHIPPED">Enviado</option>
          <option value="DELIVERED">Entregue</option>
          <option value="CANCELLED">Cancelado</option>
          <option value="REFUNDED">Reembolsado</option>
        </select>
      </div>
      
      <p>Total de pedidos: {orders.length}</p>
      
      {orders.length === 0 ? (
        <div className="no-orders">
          <p>Nenhum pedido encontrado</p>
        </div>
      ) : (
        <div className="orders-table">
          <table>
            <thead>
              <tr>
                <th>Código</th>
                <th>Cliente ID</th>
                <th>Farmácia ID</th>
                <th>Status</th>
                <th>Total</th>
                <th>Data</th>
                <th>Itens</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr key={order.publicId}>
                  <td>{order.orderCode}</td>
                  <td>{order.customerId}</td>
                  <td>{order.pharmacyId}</td>
                  <td><span className={getStatusClass(order.orderStatus)}>{getStatusLabel(order.orderStatus)}</span></td>
                  <td>R$ {order.total.toFixed(2)}</td>
                  <td>{new Date(order.createdAt).toLocaleDateString('pt-BR')}</td>
                  <td>{order.items?.length || 0} item(ns)</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default AdminOrders;
