import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import Pagination from '../common/Pagination';
import './Orders.css';

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

const Orders: React.FC = () => {
  const { showToast } = useToast();
  const [orders, setOrders] = useState<Order[]>([]);
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    try {
      const response = await api.get('/orders');
      setOrders(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar pedidos:', err);
      setError('Erro ao carregar pedidos');
    } finally {
      setLoading(false);
    }
  };

  const getOrderDetails = async (publicId: string) => {
    try {
      const response = await api.get(`/orders/${publicId}`);
      setSelectedOrder(response.data);
    } catch (err: any) {
      console.error('Erro ao carregar detalhes do pedido:', err);
      alert('Erro ao carregar detalhes do pedido');
    }
  };

  const cancelOrder = async (publicId: string) => {
    if (!window.confirm('Deseja realmente cancelar este pedido?')) return;

    try {
      await api.patch(`/orders/${publicId}/status?status=CANCELLED`);
      showToast('Pedido cancelado com sucesso!', 'success');
      loadOrders();
    } catch (err: any) {
      console.error('Erro ao cancelar pedido:', err);
      showToast(err.response?.data?.message || 'Erro ao cancelar pedido', 'error');
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

  const paginatedOrders = orders.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  if (loading) {
    return (
      <div className="orders">
        <LoadingSpinner message="Carregando pedidos..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="orders">
        <ErrorMessage message={error} onRetry={loadOrders} />
      </div>
    );
  }

  return (
    <div className="orders">
      <h2>Meus Pedidos</h2>
      
      {orders.length === 0 ? (
        <div className="no-orders">
          <p>Nenhum pedido encontrado</p>
        </div>
      ) : (
        <>
          <div className="orders-list">
            {paginatedOrders.map((order) => (
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
              <div className="order-actions">
                <button 
                  onClick={() => getOrderDetails(order.publicId)}
                  className="btn btn-primary"
                >
                  Ver Detalhes
                </button>
                {order.orderStatus === 'PENDING' && (
                  <button 
                    onClick={() => cancelOrder(order.publicId)}
                    className="btn btn-danger"
                  >
                    Cancelar Pedido
                  </button>
                )}
              </div>
              
              {selectedOrder?.publicId === order.publicId && (
                <div className="order-details">
                  <h4>Detalhes do Pedido</h4>
                  <div className="order-items">
                    {selectedOrder.items?.map((item) => (
                      <div key={item.id} className="order-item">
                        <span>{item.productName}</span>
                        <span>Qtd: {item.quantity}</span>
                        <span>R$ {item.unitPrice.toFixed(2)}</span>
                        <span>Total: R$ {(item.quantity * item.unitPrice).toFixed(2)}</span>
                      </div>
                    ))}
                  </div>
                  <div className="order-totals">
                    <p>Subtotal: R$ {selectedOrder.subtotal.toFixed(2)}</p>
                    {selectedOrder.discountAmount > 0 && (
                      <p>Desconto: R$ {selectedOrder.discountAmount.toFixed(2)}</p>
                    )}
                    {selectedOrder.shippingCost > 0 && (
                      <p>Frete: R$ {selectedOrder.shippingCost.toFixed(2)}</p>
                    )}
                    <p className="total"><strong>Total: R$ {selectedOrder.total.toFixed(2)}</strong></p>
                  </div>
                  {selectedOrder.notes && (
                    <div className="order-notes">
                      <p><strong>Observações:</strong> {selectedOrder.notes}</p>
                    </div>
                  )}
                </div>
              )}
            </div>
          ))}
          </div>

          {orders.length > itemsPerPage && (
            <Pagination
              currentPage={currentPage}
              totalPages={Math.ceil(orders.length / itemsPerPage)}
              onPageChange={setCurrentPage}
              itemsPerPage={itemsPerPage}
              totalItems={orders.length}
            />
          )}
        </>
      )}
    </div>
  );
};

export default Orders;
