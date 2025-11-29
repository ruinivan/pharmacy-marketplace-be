import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import './AdminCategories.css';

interface Category {
  id: number;
  name: string;
  parent?: Category;
}

const AdminCategories: React.FC = () => {
  const { showToast } = useToast();
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingCategory, setEditingCategory] = useState<Category | null>(null);
  const [formData, setFormData] = useState({ name: '', parentId: '' });

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      const response = await api.get('/categories');
      setCategories(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar categorias:', err);
      setError('Erro ao carregar categorias');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const data: any = { name: formData.name };
      if (formData.parentId) {
        data.parent = { id: parseInt(formData.parentId) };
      }
      
      if (editingCategory) {
        await api.put(`/categories/${editingCategory.id}`, data);
        showToast('Categoria atualizada com sucesso!', 'success');
      } else {
        await api.post('/categories', data);
        showToast('Categoria criada com sucesso!', 'success');
      }
      setShowForm(false);
      setEditingCategory(null);
      setFormData({ name: '', parentId: '' });
      loadCategories();
    } catch (err: any) {
      showToast(err.response?.data?.message || 'Erro ao salvar categoria', 'error');
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Deseja realmente excluir esta categoria?')) return;
    try {
      await api.delete(`/categories/${id}`);
      showToast('Categoria excluída com sucesso!', 'success');
      loadCategories();
    } catch (err: any) {
      showToast(err.response?.data?.message || 'Erro ao excluir categoria', 'error');
    }
  };

  const startEdit = (category: Category) => {
    setEditingCategory(category);
    setFormData({ 
      name: category.name, 
      parentId: category.parent?.id?.toString() || '' 
    });
    setShowForm(true);
  };

  if (loading) return <LoadingSpinner message="Carregando categorias..." />;
  if (error) return <ErrorMessage message={error} onRetry={loadCategories} />;

  return (
    <div className="admin-categories">
      <div className="categories-header">
        <h2>Gerenciamento de Categorias</h2>
        <button onClick={() => { setEditingCategory(null); setFormData({ name: '', parentId: '' }); setShowForm(true); }} className="btn btn-primary">
          ➕ Criar Categoria
        </button>
      </div>

      <div className="categories-list">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Pai</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {categories.map((cat) => (
              <tr key={cat.id}>
                <td>{cat.id}</td>
                <td>{cat.name}</td>
                <td>{cat.parent?.name || '-'}</td>
                <td>
                  <button onClick={() => startEdit(cat)} className="btn btn-primary btn-sm">Editar</button>
                  <button onClick={() => handleDelete(cat.id)} className="btn btn-danger btn-sm">Excluir</button>
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
              <h3>{editingCategory ? 'Editar Categoria' : 'Criar Categoria'}</h3>
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
              <div className="form-group">
                <label>Categoria Pai</label>
                <select
                  value={formData.parentId}
                  onChange={(e) => setFormData({ ...formData, parentId: e.target.value })}
                >
                  <option value="">Nenhuma</option>
                  {categories.filter(c => !editingCategory || c.id !== editingCategory.id).map(cat => (
                    <option key={cat.id} value={cat.id}>{cat.name}</option>
                  ))}
                </select>
              </div>
              <div className="form-actions">
                <button type="button" onClick={() => setShowForm(false)} className="btn btn-secondary">Cancelar</button>
                <button type="submit" className="btn btn-primary">{editingCategory ? 'Atualizar' : 'Criar'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminCategories;

