import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import Pagination from '../common/Pagination';
import './PharmacyOrders.css';

interface OrderItem {
  id: number;
  productVariantId: number;
  productVariantSku: string;
  productName: string;
  quantity: number;
  unitPrice: number;
}

interface Order {
  publicId: string;
  orderCode: string;
  customerId: number;
  pharmacyId: number;
  orderStatus: string;
  subtotal: number;
  discountAmount: number;
  shippingCost: number;
  total: number;
  notes?: string;
  items: OrderItem[];
  createdAt: string;
  updatedAt: string;
}

interface Pharmacy {
  id: number;
  tradeName: string;
}

const PharmacyOrders: React.FC = () => {
  const { user } = useAuth();
  const { showToast } = useToast();
  const [pharmacy, setPharmacy] = useState<Pharmacy | null>(null);
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterStatus, setFilterStatus] = useState<string>('ALL');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);

  useEffect(() => {
    loadPharmacyAndOrders();
  }, [user]);

  const loadPharmacyAndOrders = async () => {
    try {
      setLoading(true);
      if (!user?.email) {
        setError('Usuário não autenticado');
        return;
      }

      // Busca a farmácia pelo email
      const pharmacyResponse = await api.get(`/pharmacies/by-email/${encodeURIComponent(user.email)}`);
      setPharmacy(pharmacyResponse.data);

      // Busca pedidos da farmácia
      const ordersResponse = await api.get(`/orders/pharmacy/${pharmacyResponse.data.id}`);
      setOrders(ordersResponse.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar pedidos:', err);
      if (err.response?.status === 404) {
        setError('Farmácia não encontrada. Verifique se o usuário está associado a uma farmácia.');
      } else {
        setError('Erro ao carregar pedidos');
      }
    } finally {
      setLoading(false);
    }
  };

  const updateOrderStatus = async (orderPublicId: string, status: string) => {
    try {
      await api.patch(`/orders/${orderPublicId}/status?status=${status}`);
      showToast('Status do pedido atualizado com sucesso!', 'success');
      loadPharmacyAndOrders();
    } catch (err: any) {
      console.error('Erro ao atualizar status:', err);
      showToast(err.response?.data?.message || 'Erro ao atualizar status', 'error');
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

  const getNextStatus = (currentStatus: string): string | null => {
    const statusFlow: { [key: string]: string } = {
      'PENDING': 'PROCESSING',
      'AWAITING_PAYMENT': 'PROCESSING',
      'AWAITING_PRESCRIPTION': 'PROCESSING',
      'PROCESSING': 'SHIPPED',
      'SHIPPED': 'DELIVERED'
    };
    return statusFlow[currentStatus] || null;
  };

  const filteredOrders = filterStatus === 'ALL' 
    ? orders 
    : orders.filter(order => order.orderStatus === filterStatus);

  const paginatedOrders = filteredOrders.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  useEffect(() => {
    setCurrentPage(1); // Reset page when filter changes
  }, [filterStatus]);

  if (loading) {
    return (
      <div className="pharmacy-orders">
        <LoadingSpinner message="Carregando pedidos..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="pharmacy-orders">
        <ErrorMessage message={error} onRetry={loadPharmacyAndOrders} />
      </div>
    );
  }

  return (
    <div className="pharmacy-orders">
      <h2>Pedidos da Farmácia</h2>
      {pharmacy && <p className="pharmacy-name">{pharmacy.tradeName}</p>}
      
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
        </select>
      </div>

      {filteredOrders.length === 0 ? (
        <div className="no-orders">
          <p>Nenhum pedido encontrado</p>
        </div>
      ) : (
        <>
          <div className="orders-list">
            {paginatedOrders.map((order) => {
            const nextStatus = getNextStatus(order.orderStatus);
            return (
              <div key={order.publicId} className="order-card">
                <div className="order-header">
                  <h3>Pedido {order.orderCode}</h3>
                  <span className={`status-badge ${getStatusClass(order.orderStatus)}`}>
                    {getStatusLabel(order.orderStatus)}
                  </span>
                </div>
                <div className="order-info">
                  <p><strong>Data:</strong> {new Date(order.createdAt).toLocaleDateString('pt-BR')}</p>
                  <p><strong>Total:</strong> R$ {order.total.toFixed(2)}</p>
                  <p><strong>Itens:</strong> {order.items?.length || 0}</p>
                </div>
                <div className="order-items-preview">
                  {order.items?.slice(0, 3).map((item) => (
                    <span key={item.id} className="item-preview">
                      {item.productName} (x{item.quantity})
                    </span>
                  ))}
                  {order.items && order.items.length > 3 && (
                    <span>... e mais {order.items.length - 3} itens</span>
                  )}
                </div>
                <div className="order-actions">
                  {nextStatus && (
                    <button 
                      onClick={() => updateOrderStatus(order.publicId, nextStatus)}
                      className="btn btn-primary"
                    >
                      {nextStatus === 'PROCESSING' && 'Iniciar Processamento'}
                      {nextStatus === 'SHIPPED' && 'Marcar como Enviado'}
                      {nextStatus === 'DELIVERED' && 'Marcar como Entregue'}
                    </button>
                  )}
                  {order.orderStatus === 'PENDING' && (
                    <button 
                      onClick={() => updateOrderStatus(order.publicId, 'CANCELLED')}
                      className="btn btn-danger"
                    >
                      Cancelar Pedido
                    </button>
                  )}
                </div>
              </div>
            );
          })}
          </div>

          {filteredOrders.length > itemsPerPage && (
            <Pagination
              currentPage={currentPage}
              totalPages={Math.ceil(filteredOrders.length / itemsPerPage)}
              onPageChange={setCurrentPage}
              itemsPerPage={itemsPerPage}
              totalItems={filteredOrders.length}
            />
          )}
        </>
      )}
    </div>
  );
};

export default PharmacyOrders;
