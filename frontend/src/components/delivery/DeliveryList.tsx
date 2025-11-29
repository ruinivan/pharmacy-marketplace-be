import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import Pagination from '../common/Pagination';
import './DeliveryList.css';

interface Delivery {
  id: number;
  orderId: number;
  orderCode: string;
  deliveryPersonnelId?: number;
  deliveryPersonnelName?: string;
  addressId: number;
  addressStreet: string;
  deliveryStatus: string;
  estimatedDeliveryDate?: string;
  actualDeliveryDate?: string;
  trackingCode: string;
  notes?: string;
}

const DeliveryList: React.FC = () => {
  const { showToast } = useToast();
  const [deliveries, setDeliveries] = useState<Delivery[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterStatus, setFilterStatus] = useState<string>('PENDING');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);
  const [selectedDelivery, setSelectedDelivery] = useState<Delivery | null>(null);
  const [updateNotes, setUpdateNotes] = useState('');

  useEffect(() => {
    loadDeliveries();
  }, [filterStatus]);

  const loadDeliveries = async () => {
    try {
      setLoading(true);
      const response = await api.get(`/deliveries/status/${filterStatus}`);
      setDeliveries(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar entregas:', err);
      setError('Erro ao carregar entregas');
    } finally {
      setLoading(false);
    }
  };

  const updateDeliveryStatus = async (deliveryId: number, status: string) => {
    try {
      await api.put(`/deliveries/${deliveryId}`, {
        deliveryStatus: status,
        notes: updateNotes || undefined
      });
      showToast('Status da entrega atualizado com sucesso!', 'success');
      setSelectedDelivery(null);
      setUpdateNotes('');
      loadDeliveries();
    } catch (err: any) {
      console.error('Erro ao atualizar entrega:', err);
      showToast(err.response?.data?.message || 'Erro ao atualizar entrega', 'error');
    }
  };

  const getStatusLabel = (status: string): string => {
    const statusMap: { [key: string]: string } = {
      'PENDING': 'Pendente',
      'ASSIGNED': 'Atribuída',
      'IN_TRANSIT': 'Em Trânsito',
      'DELIVERED': 'Entregue',
      'FAILED': 'Falhou',
      'CANCELLED': 'Cancelada'
    };
    return statusMap[status] || status;
  };

  const getStatusClass = (status: string): string => {
    const classMap: { [key: string]: string } = {
      'PENDING': 'status-pending',
      'ASSIGNED': 'status-assigned',
      'IN_TRANSIT': 'status-transit',
      'DELIVERED': 'status-delivered',
      'FAILED': 'status-failed',
      'CANCELLED': 'status-cancelled'
    };
    return classMap[status] || '';
  };

  const getNextStatus = (currentStatus: string): string | null => {
    const statusFlow: { [key: string]: string } = {
      'PENDING': 'ASSIGNED',
      'ASSIGNED': 'IN_TRANSIT',
      'IN_TRANSIT': 'DELIVERED'
    };
    return statusFlow[currentStatus] || null;
  };

  const paginatedDeliveries = deliveries.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  useEffect(() => {
    setCurrentPage(1); // Reset page when filter changes
  }, [filterStatus]);

  if (loading) {
    return (
      <div className="delivery-list">
        <LoadingSpinner message="Carregando entregas..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="delivery-list">
        <ErrorMessage message={error} onRetry={loadDeliveries} />
      </div>
    );
  }

  return (
    <div className="delivery-list">
      <h2>Entregas</h2>
      
      <div className="filter-section">
        <label>Filtrar por Status:</label>
        <select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
          <option value="PENDING">Pendente</option>
          <option value="ASSIGNED">Atribuída</option>
          <option value="IN_TRANSIT">Em Trânsito</option>
          <option value="DELIVERED">Entregue</option>
          <option value="FAILED">Falhou</option>
          <option value="CANCELLED">Cancelada</option>
        </select>
      </div>

      {deliveries.length === 0 ? (
        <div className="no-deliveries">
          <p>Nenhuma entrega encontrada com o status selecionado</p>
        </div>
      ) : (
        <>
          <div className="deliveries-list">
            {paginatedDeliveries.map((delivery) => {
            const nextStatus = getNextStatus(delivery.deliveryStatus);
            return (
              <div key={delivery.id} className="delivery-card">
                <div className="delivery-header">
                  <h3>Entrega {delivery.trackingCode}</h3>
                  <span className={`status-badge ${getStatusClass(delivery.deliveryStatus)}`}>
                    {getStatusLabel(delivery.deliveryStatus)}
                  </span>
                </div>
                <div className="delivery-info">
                  <p><strong>Pedido:</strong> {delivery.orderCode}</p>
                  <p><strong>Endereço:</strong> {delivery.addressStreet}</p>
                  {delivery.estimatedDeliveryDate && (
                    <p><strong>Previsão:</strong> {new Date(delivery.estimatedDeliveryDate).toLocaleDateString('pt-BR')}</p>
                  )}
                  {delivery.actualDeliveryDate && (
                    <p><strong>Entregue em:</strong> {new Date(delivery.actualDeliveryDate).toLocaleDateString('pt-BR')}</p>
                  )}
                  {delivery.notes && (
                    <p><strong>Observações:</strong> {delivery.notes}</p>
                  )}
                </div>
                <div className="delivery-actions">
                  {nextStatus && (
                    <button 
                      onClick={() => setSelectedDelivery(delivery)}
                      className="btn btn-primary"
                    >
                      {nextStatus === 'ASSIGNED' && 'Aceitar Entrega'}
                      {nextStatus === 'IN_TRANSIT' && 'Iniciar Entrega'}
                      {nextStatus === 'DELIVERED' && 'Marcar como Entregue'}
                    </button>
                  )}
                  {delivery.deliveryStatus === 'IN_TRANSIT' && (
                    <button 
                      onClick={() => updateDeliveryStatus(delivery.id, 'DELIVERED')}
                      className="btn btn-success"
                    >
                      Finalizar Entrega
                    </button>
                  )}
                </div>

                {selectedDelivery?.id === delivery.id && (
                  <div className="delivery-update-form">
                    <h4>Atualizar Entrega</h4>
                    <div className="form-group">
                      <label>Observações:</label>
                      <textarea
                        value={updateNotes}
                        onChange={(e) => setUpdateNotes(e.target.value)}
                        rows={3}
                        placeholder="Adicione observações sobre a entrega..."
                      />
                    </div>
                    <div className="form-actions">
                      <button 
                        onClick={() => nextStatus && updateDeliveryStatus(delivery.id, nextStatus)}
                        className="btn btn-primary"
                      >
                        Confirmar
                      </button>
                      <button 
                        onClick={() => {
                          setSelectedDelivery(null);
                          setUpdateNotes('');
                        }}
                        className="btn btn-secondary"
                      >
                        Cancelar
                      </button>
                    </div>
                  </div>
                )}
              </div>
            );
          })}
          </div>

          {deliveries.length > itemsPerPage && (
            <Pagination
              currentPage={currentPage}
              totalPages={Math.ceil(deliveries.length / itemsPerPage)}
              onPageChange={setCurrentPage}
              itemsPerPage={itemsPerPage}
              totalItems={deliveries.length}
            />
          )}
        </>
      )}
    </div>
  );
};

export default DeliveryList;
