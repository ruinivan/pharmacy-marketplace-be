import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import './AdminBrands.css';

interface Brand {
  id: number;
  name: string;
}

const AdminBrands: React.FC = () => {
  const { showToast } = useToast();
  const [brands, setBrands] = useState<Brand[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingBrand, setEditingBrand] = useState<Brand | null>(null);
  const [formData, setFormData] = useState({ name: '' });

  useEffect(() => {
    loadBrands();
  }, []);

  const loadBrands = async () => {
    try {
      const response = await api.get('/brands');
      setBrands(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar marcas:', err);
      setError('Erro ao carregar marcas');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingBrand) {
        await api.put(`/brands/${editingBrand.id}`, formData);
        showToast('Marca atualizada com sucesso!', 'success');
      } else {
        await api.post('/brands', formData);
        showToast('Marca criada com sucesso!', 'success');
      }
      setShowForm(false);
      setEditingBrand(null);
      setFormData({ name: '' });
      loadBrands();
    } catch (err: any) {
      showToast(err.response?.data?.message || 'Erro ao salvar marca', 'error');
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Deseja realmente excluir esta marca?')) return;
    try {
      await api.delete(`/brands/${id}`);
      showToast('Marca excluída com sucesso!', 'success');
      loadBrands();
    } catch (err: any) {
      showToast(err.response?.data?.message || 'Erro ao excluir marca', 'error');
    }
  };

  const startEdit = (brand: Brand) => {
    setEditingBrand(brand);
    setFormData({ name: brand.name });
    setShowForm(true);
  };

  if (loading) return <LoadingSpinner message="Carregando marcas..." />;
  if (error) return <ErrorMessage message={error} onRetry={loadBrands} />;

  return (
    <div className="admin-brands">
      <div className="brands-header">
        <h2>Gerenciamento de Marcas</h2>
        <button onClick={() => { setEditingBrand(null); setFormData({ name: '' }); setShowForm(true); }} className="btn btn-primary">
          ➕ Criar Marca
        </button>
      </div>

      <div className="brands-list">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {brands.map((brand) => (
              <tr key={brand.id}>
                <td>{brand.id}</td>
                <td>{brand.name}</td>
                <td>
                  <button onClick={() => startEdit(brand)} className="btn btn-primary btn-sm">Editar</button>
                  <button onClick={() => handleDelete(brand.id)} className="btn btn-danger btn-sm">Excluir</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showForm && (
        <div className="modal-overlay" onClick={() => setShowForm(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="form-header">
              <h3>{editingBrand ? 'Editar Marca' : 'Criar Marca'}</h3>
              <button onClick={() => setShowForm(false)} className="btn-close">×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Nome *</label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  required
                />
              </div>
              <div className="form-actions">
                <button type="button" onClick={() => setShowForm(false)} className="btn btn-secondary">Cancelar</button>
                <button type="submit" className="btn btn-primary">{editingBrand ? 'Atualizar' : 'Criar'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminBrands;

