import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';
import { useToast } from '../../contexts/ToastContext';
import './PromotionForm.css';

interface Pharmacy {
  id: number;
  tradeName: string;
}

interface PromotionFormData {
  name: string;
  description: string;
  discountType: string;
  discountValue: number;
  startDate: string;
  endDate: string;
  isActive: boolean;
  maxUses: number | null;
  pharmacyId: number | null;
}

interface PromotionFormProps {
  promotion?: any;
  onClose: () => void;
  onSuccess: () => void;
}

const PromotionForm: React.FC<PromotionFormProps> = ({ promotion, onClose, onSuccess }) => {
  const { user } = useAuth();
  const { showToast } = useToast();
  const [pharmacy, setPharmacy] = useState<Pharmacy | null>(null);
  const [formData, setFormData] = useState<PromotionFormData>({
    name: promotion?.name || '',
    description: promotion?.description || '',
    discountType: promotion?.discountType || 'PERCENTAGE',
    discountValue: promotion?.discountValue || 0,
    startDate: promotion?.startDate ? new Date(promotion.startDate).toISOString().slice(0, 16) : '',
    endDate: promotion?.endDate ? new Date(promotion.endDate).toISOString().slice(0, 16) : '',
    isActive: promotion?.isActive !== undefined ? promotion.isActive : true,
    maxUses: promotion?.maxUses || null,
    pharmacyId: promotion?.pharmacyId || null
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [loadingPharmacy, setLoadingPharmacy] = useState(true);

  useEffect(() => {
    loadPharmacy();
  }, [user]);

  const loadPharmacy = async () => {
    try {
      if (!user?.email) {
        setError('Usuário não autenticado');
        return;
      }

      const response = await api.get(`/pharmacies/by-email/${encodeURIComponent(user.email)}`);
      const pharmacyData = response.data;
      setPharmacy(pharmacyData);
      setFormData(prev => ({ ...prev, pharmacyId: pharmacyData.id }));
    } catch (err: any) {
      console.error('Erro ao carregar farmácia:', err);
      setError('Erro ao carregar dados da farmácia');
    } finally {
      setLoadingPharmacy(false);
    }
  };

  const handleInputChange = (field: keyof PromotionFormData, value: any) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const validateForm = (): boolean => {
    if (!formData.name.trim()) {
      setError('Nome da promoção é obrigatório');
      return false;
    }
    if (!formData.discountValue || formData.discountValue <= 0) {
      setError('Valor do desconto deve ser maior que zero');
      return false;
    }
    if (formData.discountType === 'PERCENTAGE' && formData.discountValue > 100) {
      setError('Desconto percentual não pode ser maior que 100%');
      return false;
    }
    if (!formData.startDate) {
      setError('Data de início é obrigatória');
      return false;
    }
    if (!formData.endDate) {
      setError('Data de término é obrigatória');
      return false;
    }
    if (new Date(formData.endDate) <= new Date(formData.startDate)) {
      setError('Data de término deve ser posterior à data de início');
      return false;
    }
    if (!formData.pharmacyId) {
      setError('Farmácia é obrigatória');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!validateForm()) {
      return;
    }

    setLoading(true);
    try {
      const requestData = {
        name: formData.name,
        description: formData.description || null,
        discountType: formData.discountType,
        discountValue: formData.discountValue,
        startDate: new Date(formData.startDate).toISOString(),
        endDate: new Date(formData.endDate).toISOString(),
        isActive: formData.isActive,
        maxUses: formData.maxUses || null,
        pharmacyId: formData.pharmacyId,
        rules: [],
        targets: []
      };

      if (promotion) {
        await api.put(`/promotions/${promotion.id}`, requestData);
        showToast('Promoção atualizada com sucesso!', 'success');
      } else {
        await api.post('/promotions', requestData);
        showToast('Promoção criada com sucesso!', 'success');
      }
      onSuccess();
      onClose();
    } catch (err: any) {
      console.error('Erro ao salvar promoção:', err);
      const errorMsg = err.response?.data?.message || 'Erro ao salvar promoção';
      setError(errorMsg);
      showToast(errorMsg, 'error');
    } finally {
      setLoading(false);
    }
  };

  if (loadingPharmacy) {
    return (
      <div className="modal-overlay">
        <div className="modal-content">
          <p>Carregando dados...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content promotion-form" onClick={(e) => e.stopPropagation()}>
        <div className="form-header">
          <h2>{promotion ? 'Editar Promoção' : 'Criar Nova Promoção'}</h2>
          <button onClick={onClose} className="btn-close">×</button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-section">
            <h3>Informações Básicas</h3>
            
            <div className="form-group">
              <label>Nome da Promoção *</label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) => handleInputChange('name', e.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label>Descrição</label>
              <textarea
                value={formData.description}
                onChange={(e) => handleInputChange('description', e.target.value)}
                rows={3}
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Tipo de Desconto *</label>
                <select
                  value={formData.discountType}
                  onChange={(e) => handleInputChange('discountType', e.target.value)}
                  required
                >
                  <option value="PERCENTAGE">Percentual (%)</option>
                  <option value="FIXED">Valor Fixo (R$)</option>
                </select>
              </div>

              <div className="form-group">
                <label>Valor do Desconto *</label>
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  value={formData.discountValue}
                  onChange={(e) => handleInputChange('discountValue', Number(e.target.value))}
                  required
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Data de Início *</label>
                <input
                  type="datetime-local"
                  value={formData.startDate}
                  onChange={(e) => handleInputChange('startDate', e.target.value)}
                  required
                />
              </div>

              <div className="form-group">
                <label>Data de Término *</label>
                <input
                  type="datetime-local"
                  value={formData.endDate}
                  onChange={(e) => handleInputChange('endDate', e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Máximo de Usos</label>
                <input
                  type="number"
                  min="1"
                  value={formData.maxUses || ''}
                  onChange={(e) => handleInputChange('maxUses', e.target.value ? Number(e.target.value) : null)}
                  placeholder="Ilimitado se vazio"
                />
              </div>

              <div className="form-group">
                <label>
                  <input
                    type="checkbox"
                    checked={formData.isActive}
                    onChange={(e) => handleInputChange('isActive', e.target.checked)}
                  />
                  Promoção Ativa
                </label>
              </div>
            </div>

            {pharmacy && (
              <div className="form-group">
                <label>Farmácia</label>
                <input
                  type="text"
                  value={pharmacy.tradeName}
                  disabled
                  className="disabled-input"
                />
              </div>
            )}
          </div>

          <div className="form-actions">
            <button type="button" onClick={onClose} className="btn btn-secondary">
              Cancelar
            </button>
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Salvando...' : (promotion ? 'Atualizar' : 'Criar')}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PromotionForm;

