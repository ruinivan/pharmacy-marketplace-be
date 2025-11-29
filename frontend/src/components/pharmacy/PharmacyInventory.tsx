import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import './PharmacyInventory.css';

interface InventoryItem {
  pharmacyId: number;
  productVariantId: number;
  productVariantSku: string;
  productName: string;
  quantity: number;
  price: number;
}

interface Pharmacy {
  id: number;
  tradeName: string;
  legalName: string;
}

interface Product {
  publicId: string;
  name: string;
  variants: ProductVariant[];
}

interface ProductVariant {
  id: number;
  sku: string;
  dosage: string;
  packageSize: string;
}

const PharmacyInventory: React.FC = () => {
  const { user } = useAuth();
  const { showToast } = useToast();
  const [pharmacy, setPharmacy] = useState<Pharmacy | null>(null);
  const [inventory, setInventory] = useState<InventoryItem[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editingItem, setEditingItem] = useState<InventoryItem | null>(null);
  const [editQuantity, setEditQuantity] = useState<number>(0);
  const [editPrice, setEditPrice] = useState<number>(0);
  const [showAddForm, setShowAddForm] = useState(false);
  const [selectedProductVariantId, setSelectedProductVariantId] = useState<number | null>(null);
  const [newQuantity, setNewQuantity] = useState<number>(0);
  const [newPrice, setNewPrice] = useState<number>(0);

  useEffect(() => {
    loadPharmacyAndInventory();
    loadProducts();
  }, [user]);

  const loadPharmacyAndInventory = async () => {
    try {
      setLoading(true);
      if (!user?.email) {
        setError('Usuário não autenticado');
        return;
      }

      const pharmacyResponse = await api.get(`/pharmacies/by-email/${encodeURIComponent(user.email)}`);
      const pharmacyData = pharmacyResponse.data;
      setPharmacy(pharmacyData);

      const inventoryResponse = await api.get(`/inventory/pharmacy/${pharmacyData.id}`);
      setInventory(inventoryResponse.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar inventário:', err);
      if (err.response?.status === 404) {
        setError('Farmácia não encontrada. Verifique se o usuário está associado a uma farmácia.');
      } else {
        setError('Erro ao carregar inventário. Tente novamente.');
      }
    } finally {
      setLoading(false);
    }
  };

  const loadProducts = async () => {
    try {
      const response = await api.get('/products');
      setProducts(response.data);
    } catch (err) {
      console.error('Erro ao carregar produtos:', err);
    }
  };

  const startEdit = (item: InventoryItem) => {
    setEditingItem(item);
    setEditQuantity(item.quantity);
    setEditPrice(item.price);
  };

  const cancelEdit = () => {
    setEditingItem(null);
    setEditQuantity(0);
    setEditPrice(0);
  };

  const saveEdit = async () => {
    if (!editingItem) return;

    try {
      await api.put(
        `/inventory/pharmacy/${editingItem.pharmacyId}/product-variant/${editingItem.productVariantId}`,
        {
          quantity: editQuantity,
          price: editPrice
        }
      );
      showToast('Inventário atualizado com sucesso!', 'success');
      cancelEdit();
      loadPharmacyAndInventory();
    } catch (err: any) {
      console.error('Erro ao atualizar inventário:', err);
      showToast(err.response?.data?.message || 'Erro ao atualizar inventário', 'error');
    }
  };

  const handleAddItem = async () => {
    if (!pharmacy || !selectedProductVariantId || newQuantity <= 0 || newPrice <= 0) {
      showToast('Preencha todos os campos corretamente', 'warning');
      return;
    }

    try {
      await api.put(
        `/inventory/pharmacy/${pharmacy.id}/product-variant/${selectedProductVariantId}`,
        {
          quantity: newQuantity,
          price: newPrice
        }
      );
      showToast('Produto adicionado ao inventário!', 'success');
      setShowAddForm(false);
      setSelectedProductVariantId(null);
      setNewQuantity(0);
      setNewPrice(0);
      loadPharmacyAndInventory();
    } catch (err: any) {
      console.error('Erro ao adicionar produto:', err);
      showToast(err.response?.data?.message || 'Erro ao adicionar produto', 'error');
    }
  };

  const deleteItem = async (item: InventoryItem) => {
    if (!window.confirm(`Deseja remover ${item.productName} do inventário?`)) return;

    try {
      await api.delete(
        `/inventory/pharmacy/${item.pharmacyId}/product-variant/${item.productVariantId}`
      );
      showToast('Item removido do inventário!', 'success');
      loadPharmacyAndInventory();
    } catch (err: any) {
      console.error('Erro ao remover item:', err);
      showToast('Erro ao remover item do inventário', 'error');
    }
  };

  if (loading) {
    return (
      <div className="pharmacy-inventory">
        <LoadingSpinner message="Carregando inventário..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="pharmacy-inventory">
        <ErrorMessage message={error} onRetry={loadPharmacyAndInventory} />
      </div>
    );
  }

  return (
    <div className="pharmacy-inventory">
      <div className="inventory-header">
        <h2>Estoque da Farmácia</h2>
        <button 
          onClick={() => setShowAddForm(true)} 
          className="btn btn-primary"
        >
          Adicionar Produto
        </button>
      </div>
      {pharmacy && (
        <div className="pharmacy-info">
          <h3>{pharmacy.tradeName}</h3>
          <p>{pharmacy.legalName}</p>
        </div>
      )}
      
      <p>Total de itens em estoque: {inventory.length}</p>

      {showAddForm && (
        <div className="modal-overlay" onClick={() => setShowAddForm(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="form-header">
              <h3>Adicionar Produto ao Estoque</h3>
              <button onClick={() => setShowAddForm(false)} className="btn-close">×</button>
            </div>
            <div className="form-group">
              <label>Produto e Variante *</label>
              <select
                value={selectedProductVariantId || ''}
                onChange={(e) => setSelectedProductVariantId(Number(e.target.value) || null)}
                required
              >
                <option value="">Selecione um produto</option>
                {products.map(product =>
                  product.variants?.map(variant => (
                    <option key={variant.id} value={variant.id}>
                      {product.name} - {variant.sku} ({variant.packageSize})
                    </option>
                  ))
                )}
              </select>
            </div>
            <div className="form-group">
              <label>Quantidade *</label>
              <input
                type="number"
                min="1"
                value={newQuantity}
                onChange={(e) => setNewQuantity(Number(e.target.value))}
                required
              />
            </div>
            <div className="form-group">
              <label>Preço (R$) *</label>
              <input
                type="number"
                min="0"
                step="0.01"
                value={newPrice}
                onChange={(e) => setNewPrice(Number(e.target.value))}
                required
              />
            </div>
            <div className="form-actions">
              <button onClick={() => setShowAddForm(false)} className="btn btn-secondary">
                Cancelar
              </button>
              <button onClick={handleAddItem} className="btn btn-primary">
                Adicionar
              </button>
            </div>
          </div>
        </div>
      )}
      
      {inventory.length === 0 ? (
        <div className="no-inventory">
          <p>Nenhum produto em estoque.</p>
          <p>Os produtos e estoques serão criados automaticamente pelo seed do sistema.</p>
        </div>
      ) : (
        <div className="inventory-table">
          <table>
            <thead>
              <tr>
                <th>Produto</th>
                <th>SKU</th>
                <th>Quantidade</th>
                <th>Preço</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {inventory.map((item) => (
                <tr key={`${item.pharmacyId}-${item.productVariantId}`}>
                  <td>{item.productName}</td>
                  <td>{item.productVariantSku}</td>
                  <td>
                    {editingItem?.productVariantId === item.productVariantId ? (
                      <input
                        type="number"
                        min="0"
                        value={editQuantity}
                        onChange={(e) => setEditQuantity(Number(e.target.value))}
                        className="edit-input"
                      />
                    ) : (
                      <span className={item.quantity === 0 ? 'out-of-stock' : ''}>
                        {item.quantity}
                      </span>
                    )}
                  </td>
                  <td>
                    {editingItem?.productVariantId === item.productVariantId ? (
                      <input
                        type="number"
                        min="0"
                        step="0.01"
                        value={editPrice}
                        onChange={(e) => setEditPrice(Number(e.target.value))}
                        className="edit-input"
                      />
                    ) : (
                      `R$ ${item.price.toFixed(2)}`
                    )}
                  </td>
                  <td>
                    {editingItem?.productVariantId === item.productVariantId ? (
                      <div className="edit-actions">
                        <button onClick={saveEdit} className="btn btn-success btn-sm">Salvar</button>
                        <button onClick={cancelEdit} className="btn btn-secondary btn-sm">Cancelar</button>
                      </div>
                    ) : (
                      <div className="item-actions">
                        <button onClick={() => startEdit(item)} className="btn btn-primary btn-sm">
                          Editar
                        </button>
                        <button onClick={() => deleteItem(item)} className="btn btn-danger btn-sm">
                          Remover
                        </button>
                      </div>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default PharmacyInventory;
