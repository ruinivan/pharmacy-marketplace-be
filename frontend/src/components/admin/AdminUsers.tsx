import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import Pagination from '../common/Pagination';
import './AdminUsers.css';

interface User {
  id: number;
  publicId: string;
  email: string;
  fullName?: string;
  phoneNumber?: string;
  roles: string[];
  isActive: boolean;
}

const AdminUsers: React.FC = () => {
  const { showToast } = useToast();
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(15);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [showEditForm, setShowEditForm] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    phoneNumber: '',
    isActive: true
  });

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const response = await api.get('/users');
      setUsers(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar usuários:', err);
      setError('Erro ao carregar usuários');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Deseja realmente excluir este usuário?')) return;

    try {
      await api.delete(`/users/${id}`);
      showToast('Usuário excluído com sucesso!', 'success');
      loadUsers();
    } catch (err: any) {
      console.error('Erro ao excluir usuário:', err);
      showToast(err.response?.data?.message || 'Erro ao excluir usuário', 'error');
    }
  };

  const handleEdit = (user: User) => {
    setEditingUser(user);
    setFormData({
      email: user.email,
      phoneNumber: user.phoneNumber || '',
      isActive: user.isActive
    });
    setShowEditForm(true);
  };

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingUser) return;

    try {
      await api.put(`/users/${editingUser.id}`, formData);
      showToast('Usuário atualizado com sucesso!', 'success');
      setShowEditForm(false);
      setEditingUser(null);
      loadUsers();
    } catch (err: any) {
      console.error('Erro ao atualizar usuário:', err);
      showToast(err.response?.data?.message || 'Erro ao atualizar usuário', 'error');
    }
  };

  const paginatedUsers = users.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  if (loading) {
    return (
      <div className="admin-users">
        <LoadingSpinner message="Carregando usuários..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="admin-users">
        <ErrorMessage message={error} onRetry={loadUsers} />
      </div>
    );
  }

  return (
    <div className="admin-users">
      <h2>Gerenciamento de Usuários</h2>
      <p>Total de usuários: {users.length}</p>
      
      {users.length === 0 ? (
        <div className="no-users">
          <p>Nenhum usuário encontrado</p>
        </div>
      ) : (
        <div className="users-table">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Email</th>
                <th>Nome</th>
                <th>Telefone</th>
                <th>Roles</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {paginatedUsers.map((user) => (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>{user.email}</td>
                  <td>{user.fullName || '-'}</td>
                  <td>{user.phoneNumber || '-'}</td>
                  <td>
                    <div className="roles-list">
                      {user.roles?.map((role, idx) => (
                        <span key={idx} className="role-badge">{role}</span>
                      ))}
                    </div>
                  </td>
                  <td>
                    <span className={user.isActive ? 'status-active' : 'status-inactive'}>
                      {user.isActive ? 'Ativo' : 'Inativo'}
                    </span>
                  </td>
                  <td>
                    <button 
                      onClick={() => handleEdit(user)} 
                      className="btn btn-primary btn-sm"
                    >
                      Editar
                    </button>
                    <button 
                      onClick={() => handleDelete(user.id)} 
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

      {users.length > itemsPerPage && (
        <Pagination
          currentPage={currentPage}
          totalPages={Math.ceil(users.length / itemsPerPage)}
          onPageChange={setCurrentPage}
          itemsPerPage={itemsPerPage}
          totalItems={users.length}
        />
      )}

      {showEditForm && editingUser && (
        <div className="modal-overlay" onClick={() => setShowEditForm(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="form-header">
              <h3>Editar Usuário</h3>
              <button onClick={() => setShowEditForm(false)} className="btn-close">×</button>
            </div>
            <form onSubmit={handleUpdate}>
              <div className="form-group">
                <label>Email *</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Telefone</label>
                <input
                  type="text"
                  value={formData.phoneNumber}
                  onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>
                  <input
                    type="checkbox"
                    checked={formData.isActive}
                    onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
                  />
                  Ativo
                </label>
              </div>
              <div className="form-actions">
                <button type="button" onClick={() => setShowEditForm(false)} className="btn btn-secondary">
                  Cancelar
                </button>
                <button type="submit" className="btn btn-primary">
                  Atualizar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminUsers;
