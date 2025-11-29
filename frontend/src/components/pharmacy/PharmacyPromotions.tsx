import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';
import { useToast } from '../../contexts/ToastContext';
import PromotionForm from './PromotionForm';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import './PharmacyPromotions.css';

interface Promotion {
  id: number;
  name: string;
  description: string;
  discountType: string;
  discountValue: number;
  startDate: string;
  endDate: string;
  isActive: boolean;
  maxUses?: number;
  currentUses?: number;
  pharmacyId: number;
}

interface Pharmacy {
  id: number;
  tradeName: string;
}

const PharmacyPromotions: React.FC = () => {
  const { user } = useAuth();
  const { showToast } = useToast();
  const [pharmacy, setPharmacy] = useState<Pharmacy | null>(null);
  const [promotions, setPromotions] = useState<Promotion[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingPromotion, setEditingPromotion] = useState<Promotion | null>(null);

  useEffect(() => {
    loadPharmacyAndPromotions();
  }, [user]);

  const loadPharmacyAndPromotions = async () => {
    try {
      setLoading(true);
      if (!user?.email) {
        setError('Usuário não autenticado');
        return;
      }

      const pharmacyResponse = await api.get(`/pharmacies/by-email/${encodeURIComponent(user.email)}`);
      const pharmacyData = pharmacyResponse.data;
      setPharmacy(pharmacyData);

      const promotionsResponse = await api.get(`/promotions/pharmacy/${pharmacyData.id}`);
      setPromotions(promotionsResponse.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar promoções:', err);
      if (err.response?.status === 404) {
        setError('Farmácia não encontrada');
      } else {
        setError('Erro ao carregar promoções');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Deseja realmente excluir esta promoção?')) return;

    try {
      await api.delete(`/promotions/${id}`);
      showToast('Promoção excluída com sucesso!', 'success');
      loadPharmacyAndPromotions();
    } catch (err: any) {
      console.error('Erro ao excluir promoção:', err);
      showToast('Erro ao excluir promoção', 'error');
    }
  };

  if (loading) {
    return (
      <div className="pharmacy-promotions">
        <LoadingSpinner message="Carregando promoções..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="pharmacy-promotions">
        <ErrorMessage message={error} onRetry={loadPharmacyAndPromotions} />
      </div>
    );
  }

  return (
    <div className="pharmacy-promotions">
      <div className="promotions-header">
        <h2>Promoções da Farmácia</h2>
        {pharmacy && <p>{pharmacy.tradeName}</p>}
        <button 
          onClick={() => setShowCreateForm(true)} 
          className="btn btn-primary"
        >
          Criar Nova Promoção
        </button>
      </div>
      
      {promotions.length === 0 ? (
        <div className="no-promotions">
          <p>Nenhuma promoção cadastrada</p>
        </div>
      ) : (
        <div className="promotions-list">
          {promotions.map((promotion) => (
            <div key={promotion.id} className="promotion-card">
              <div className="promotion-header">
                <h3>{promotion.name}</h3>
                <span className={promotion.isActive ? 'status-active' : 'status-inactive'}>
                  {promotion.isActive ? 'Ativa' : 'Inativa'}
                </span>
              </div>
              <p>{promotion.description}</p>
              <div className="promotion-details">
                <p><strong>Tipo:</strong> {promotion.discountType}</p>
                <p><strong>Desconto:</strong> {promotion.discountValue}</p>
                <p><strong>Período:</strong> {new Date(promotion.startDate).toLocaleDateString('pt-BR')} até {new Date(promotion.endDate).toLocaleDateString('pt-BR')}</p>
                {promotion.maxUses && (
                  <p><strong>Usos:</strong> {promotion.currentUses || 0} / {promotion.maxUses}</p>
                )}
              </div>
              <div className="promotion-actions">
                <button 
                  onClick={() => {
                    setEditingPromotion(promotion);
                    setShowCreateForm(true);
                  }} 
                  className="btn btn-primary btn-sm"
                >
                  Editar
                </button>
                <button 
                  onClick={() => handleDelete(promotion.id)} 
                  className="btn btn-danger btn-sm"
                >
                  Excluir
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {showCreateForm && (
        <PromotionForm
          promotion={editingPromotion || undefined}
          onClose={() => {
            setShowCreateForm(false);
            setEditingPromotion(null);
          }}
          onSuccess={() => {
            setShowCreateForm(false);
            setEditingPromotion(null);
            loadPharmacyAndPromotions();
          }}
        />
      )}
    </div>
  );
};

export default PharmacyPromotions;

