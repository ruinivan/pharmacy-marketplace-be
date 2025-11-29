import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import Pagination from '../common/Pagination';
import './AdminPharmacies.css';

interface Pharmacy {
  id: number;
  legalName: string;
  tradeName: string;
  cnpj: string;
  phone: string;
  email: string;
  website?: string;
}

const AdminPharmacies: React.FC = () => {
  const { showToast } = useToast();
  const [pharmacies, setPharmacies] = useState<Pharmacy[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(15);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingPharmacy, setEditingPharmacy] = useState<Pharmacy | null>(null);
  const [formData, setFormData] = useState({
    legalName: '',
    tradeName: '',
    cnpj: '',
    phone: '',
    email: '',
    website: '',
      address: {
        street: '',
        neighborhood: '',
        city: '',
        state: '',
        country: 'Brasil',
        postalCode: '',
        complement: '',
        openingHours: ''
      }
  });

  useEffect(() => {
    loadPharmacies();
  }, []);

  const loadPharmacies = async () => {
    try {
      const response = await api.get('/pharmacies');
      setPharmacies(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar farmácias:', err);
      setError('Erro ao carregar farmácias');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Deseja realmente excluir esta farmácia?')) return;

    try {
      await api.delete(`/pharmacies/${id}`);
      showToast('Farmácia excluída com sucesso!', 'success');
      loadPharmacies();
    } catch (err: any) {
      console.error('Erro ao excluir farmácia:', err);
      showToast(err.response?.data?.message || 'Erro ao excluir farmácia', 'error');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingPharmacy) {
        await api.put(`/pharmacies/${editingPharmacy.id}`, formData);
        showToast('Farmácia atualizada com sucesso!', 'success');
      } else {
        await api.post('/pharmacies', formData);
        showToast('Farmácia criada com sucesso!', 'success');
      }
      setShowCreateForm(false);
      setEditingPharmacy(null);
      setFormData({
        legalName: '',
        tradeName: '',
        cnpj: '',
        phone: '',
        email: '',
        website: '',
      address: {
        street: '',
        neighborhood: '',
        city: '',
        state: '',
        country: 'Brasil',
        postalCode: '',
        complement: '',
        openingHours: ''
      }
      });
      loadPharmacies();
    } catch (err: any) {
      console.error('Erro ao salvar farmácia:', err);
      showToast(err.response?.data?.message || 'Erro ao salvar farmácia', 'error');
    }
  };

  const startEdit = (pharmacy: Pharmacy) => {
    setEditingPharmacy(pharmacy);
    // Para edição, precisamos carregar o endereço separadamente se necessário
    setFormData({
      legalName: pharmacy.legalName,
      tradeName: pharmacy.tradeName,
      cnpj: pharmacy.cnpj,
      phone: pharmacy.phone,
      email: pharmacy.email || '',
      website: pharmacy.website || '',
      address: {
        street: '',
        neighborhood: '',
        city: '',
        state: '',
        country: 'Brasil',
        postalCode: '',
        complement: '',
        openingHours: ''
      }
    });
    setShowCreateForm(true);
  };

  const paginatedPharmacies = pharmacies.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  if (loading) {
    return (
      <div className="admin-pharmacies">
        <LoadingSpinner message="Carregando farmácias..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="admin-pharmacies">
        <ErrorMessage message={error} onRetry={loadPharmacies} />
      </div>
    );
  }

  return (
    <div className="admin-pharmacies">
      <div className="pharmacies-header">
        <h2>Gerenciamento de Farmácias</h2>
        <button 
          onClick={() => {
            setEditingPharmacy(null);
            setFormData({
              legalName: '',
              tradeName: '',
              cnpj: '',
              phone: '',
              email: '',
              website: '',
              address: {
                street: '',
                neighborhood: '',
                city: '',
                state: '',
                country: 'Brasil',
                postalCode: '',
                complement: '',
                openingHours: ''
              }
            });
            setShowCreateForm(true);
          }} 
          className="btn btn-primary"
        >
          Criar Nova Farmácia
        </button>
      </div>
      <p>Total de farmácias: {pharmacies.length}</p>
      
      {pharmacies.length === 0 ? (
        <div className="no-pharmacies">
          <p>Nenhuma farmácia encontrada</p>
        </div>
      ) : (
        <div className="pharmacies-table">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome Fantasia</th>
                <th>Razão Social</th>
                <th>CNPJ</th>
                <th>Telefone</th>
                <th>Email</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {paginatedPharmacies.map((pharmacy) => (
                <tr key={pharmacy.id}>
                  <td>{pharmacy.id}</td>
                  <td>{pharmacy.tradeName}</td>
                  <td>{pharmacy.legalName}</td>
                  <td>{pharmacy.cnpj}</td>
                  <td>{pharmacy.phone}</td>
                  <td>{pharmacy.email}</td>
                  <td>
                    <button 
                      onClick={() => startEdit(pharmacy)} 
                      className="btn btn-primary btn-sm"
                    >
                      Editar
                    </button>
                    <button 
                      onClick={() => handleDelete(pharmacy.id)} 
                      className="btn btn-danger btn-sm"
                    >
                      Excluir
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {pharmacies.length > itemsPerPage && (
        <Pagination
          currentPage={currentPage}
          totalPages={Math.ceil(pharmacies.length / itemsPerPage)}
          onPageChange={setCurrentPage}
          itemsPerPage={itemsPerPage}
          totalItems={pharmacies.length}
        />
      )}

      {showCreateForm && (
        <div className="modal-overlay" onClick={() => setShowCreateForm(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="form-header">
              <h3>{editingPharmacy ? 'Editar Farmácia' : 'Criar Nova Farmácia'}</h3>
              <button onClick={() => setShowCreateForm(false)} className="btn-close">×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Razão Social *</label>
                <input
                  type="text"
                  value={formData.legalName}
                  onChange={(e) => setFormData({ ...formData, legalName: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Nome Fantasia *</label>
                <input
                  type="text"
                  value={formData.tradeName}
                  onChange={(e) => setFormData({ ...formData, tradeName: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>CNPJ *</label>
                <input
                  type="text"
                  value={formData.cnpj}
                  onChange={(e) => setFormData({ ...formData, cnpj: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Telefone *</label>
                <input
                  type="text"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Website</label>
                <input
                  type="url"
                  value={formData.website}
                  onChange={(e) => setFormData({ ...formData, website: e.target.value })}
                />
              </div>
              <div className="form-section">
                <h4>Endereço</h4>
                <div className="form-group">
                  <label>Rua *</label>
                  <input
                    type="text"
                    value={formData.address.street}
                    onChange={(e) => setFormData({ ...formData, address: { ...formData.address, street: e.target.value } })}
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Bairro *</label>
                  <input
                    type="text"
                    value={formData.address.neighborhood}
                    onChange={(e) => setFormData({ ...formData, address: { ...formData.address, neighborhood: e.target.value } })}
                    required
                  />
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>Cidade *</label>
                    <input
                      type="text"
                      value={formData.address.city}
                      onChange={(e) => setFormData({ ...formData, address: { ...formData.address, city: e.target.value } })}
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Estado *</label>
                    <input
                      type="text"
                      value={formData.address.state}
                      onChange={(e) => setFormData({ ...formData, address: { ...formData.address, state: e.target.value } })}
                      required
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>CEP</label>
                    <input
                      type="text"
                      value={formData.address.postalCode}
                      onChange={(e) => setFormData({ ...formData, address: { ...formData.address, postalCode: e.target.value } })}
                    />
                  </div>
                  <div className="form-group">
                    <label>País *</label>
                    <input
                      type="text"
                      value={formData.address.country}
                      onChange={(e) => setFormData({ ...formData, address: { ...formData.address, country: e.target.value } })}
                      required
                    />
                  </div>
                </div>
                <div className="form-group">
                  <label>Complemento</label>
                  <input
                    type="text"
                    value={formData.address.complement}
                    onChange={(e) => setFormData({ ...formData, address: { ...formData.address, complement: e.target.value } })}
                  />
                </div>
                <div className="form-group">
                  <label>Horário de Funcionamento</label>
                  <input
                    type="text"
                    value={formData.address.openingHours}
                    onChange={(e) => setFormData({ ...formData, address: { ...formData.address, openingHours: e.target.value } })}
                    placeholder="Ex: Seg-Sex: 8h-18h"
                  />
                </div>
              </div>
              <div className="form-actions">
                <button type="button" onClick={() => setShowCreateForm(false)} className="btn btn-secondary">
                  Cancelar
                </button>
                <button type="submit" className="btn btn-primary">
                  {editingPharmacy ? 'Atualizar' : 'Criar'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminPharmacies;
