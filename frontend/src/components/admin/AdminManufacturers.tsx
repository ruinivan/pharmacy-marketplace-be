import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import './AdminManufacturers.css';

interface Manufacturer {
  id: number;
  name: string;
}

const AdminManufacturers: React.FC = () => {
  const { showToast } = useToast();
  const [manufacturers, setManufacturers] = useState<Manufacturer[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingManufacturer, setEditingManufacturer] = useState<Manufacturer | null>(null);
  const [formData, setFormData] = useState({ name: '' });

  useEffect(() => {
    loadManufacturers();
  }, []);

  const loadManufacturers = async () => {
    try {
      const response = await api.get('/manufacturers');
      setManufacturers(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar fabricantes:', err);
      setError('Erro ao carregar fabricantes');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingManufacturer) {
        await api.put(`/manufacturers/${editingManufacturer.id}`, formData);
        showToast('Fabricante atualizado com sucesso!', 'success');
      } else {
        await api.post('/manufacturers', formData);
        showToast('Fabricante criado com sucesso!', 'success');
      }
      setShowForm(false);
      setEditingManufacturer(null);
      setFormData({ name: '' });
      loadManufacturers();
    } catch (err: any) {
      showToast(err.response?.data?.message || 'Erro ao salvar fabricante', 'error');
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Deseja realmente excluir este fabricante?')) return;
    try {
      await api.delete(`/manufacturers/${id}`);
      showToast('Fabricante excluído com sucesso!', 'success');
      loadManufacturers();
    } catch (err: any) {
      showToast(err.response?.data?.message || 'Erro ao excluir fabricante', 'error');
    }
  };

  const startEdit = (manufacturer: Manufacturer) => {
    setEditingManufacturer(manufacturer);
    setFormData({ name: manufacturer.name });
    setShowForm(true);
  };

  if (loading) return <LoadingSpinner message="Carregando fabricantes..." />;
  if (error) return <ErrorMessage message={error} onRetry={loadManufacturers} />;

  return (
    <div className="admin-manufacturers">
      <div className="manufacturers-header">
        <h2>Gerenciamento de Fabricantes</h2>
        <button onClick={() => { setEditingManufacturer(null); setFormData({ name: '' }); setShowForm(true); }} className="btn btn-primary">
          ➕ Criar Fabricante
        </button>
      </div>

      <div className="manufacturers-list">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {manufacturers.map((manufacturer) => (
              <tr key={manufacturer.id}>
                <td>{manufacturer.id}</td>
                <td>{manufacturer.name}</td>
                <td>
                  <button onClick={() => startEdit(manufacturer)} className="btn btn-primary btn-sm">Editar</button>
                  <button onClick={() => handleDelete(manufacturer.id)} className="btn btn-danger btn-sm">Excluir</button>
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
              <h3>{editingManufacturer ? 'Editar Fabricante' : 'Criar Fabricante'}</h3>
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
                <button type="submit" className="btn btn-primary">{editingManufacturer ? 'Atualizar' : 'Criar'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminManufacturers;

