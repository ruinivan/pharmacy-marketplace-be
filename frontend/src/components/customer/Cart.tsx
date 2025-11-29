import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import './Cart.css';

interface CartItem {
  productVariantId: number;
  productVariantSku: string;
  productName: string;
  dosage: string;
  packageSize: string;
  quantity: number;
  createdAt: string;
}

interface Cart {
  customerId: number;
  items: CartItem[];
  total?: number;
}

interface InventoryItem {
  pharmacyId: number;
  productVariantId: number;
  quantity: number;
  price: number;
  productName: string;
  productVariantSku: string;
}

const Cart: React.FC = () => {
  const navigate = useNavigate();
  const { showToast } = useToast();
  const [cart, setCart] = useState<Cart | null>(null);
  const [pharmacies, setPharmacies] = useState<any[]>([]);
  const [selectedPharmacy, setSelectedPharmacy] = useState<number | null>(null);
  const [inventory, setInventory] = useState<Map<number, InventoryItem[]>>(new Map());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [checkoutLoading, setCheckoutLoading] = useState(false);

  useEffect(() => {
    loadCart();
    loadPharmacies();
  }, []);

  useEffect(() => {
    if (selectedPharmacy) {
      loadInventory(selectedPharmacy);
    }
  }, [selectedPharmacy]);

  const loadCart = async () => {
    try {
      const response = await api.get('/cart');
      setCart(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar carrinho:', err);
      setError('Erro ao carregar carrinho');
    } finally {
      setLoading(false);
    }
  };

  const loadPharmacies = async () => {
    try {
      const response = await api.get('/pharmacies');
      setPharmacies(response.data);
    } catch (err) {
      console.error('Erro ao carregar farmácias:', err);
    }
  };

  const loadInventory = async (pharmacyId: number) => {
    try {
      const response = await api.get(`/inventory/pharmacy/${pharmacyId}`);
      const inventoryMap = new Map<number, InventoryItem[]>();
      inventoryMap.set(pharmacyId, response.data);
      setInventory(inventoryMap);
    } catch (err) {
      console.error('Erro ao carregar inventário:', err);
    }
  };

  const getItemPrice = (productVariantId: number): number | null => {
    if (!selectedPharmacy) return null;
    const items = inventory.get(selectedPharmacy);
    if (!items) return null;
    const item = items.find((i: InventoryItem) => i.productVariantId === productVariantId);
    return item ? item.price : null;
  };

  const calculateTotal = (): number => {
    if (!cart || !cart.items || !selectedPharmacy) return 0;
    return cart.items.reduce((total, item) => {
      const price = getItemPrice(item.productVariantId);
      return total + (price ? price * item.quantity : 0);
    }, 0);
  };

  const updateQuantity = async (productVariantId: number, quantity: number) => {
    if (quantity < 1) {
      removeItem(productVariantId);
      return;
    }
    try {
      await api.put(`/cart/items/${productVariantId}?quantity=${quantity}`);
      showToast('Quantidade atualizada com sucesso!', 'success');
      loadCart();
    } catch (err: any) {
      console.error('Erro ao atualizar quantidade:', err);
      showToast(err.response?.data?.message || 'Erro ao atualizar quantidade', 'error');
    }
  };

  const removeItem = async (productVariantId: number) => {
    try {
      await api.delete(`/cart/items/${productVariantId}`);
      showToast('Item removido do carrinho', 'success');
      loadCart();
    } catch (err: any) {
      console.error('Erro ao remover item:', err);
      showToast('Erro ao remover item', 'error');
    }
  };

  const clearCart = async () => {
    if (!window.confirm('Deseja limpar o carrinho?')) return;
    try {
      await api.delete('/cart');
      showToast('Carrinho limpo com sucesso!', 'success');
      loadCart();
    } catch (err: any) {
      console.error('Erro ao limpar carrinho:', err);
      showToast('Erro ao limpar carrinho', 'error');
    }
  };


  const handleCheckout = async () => {
    if (!selectedPharmacy) {
      showToast('Selecione uma farmácia', 'warning');
      return;
    }
    if (!cart || !cart.items || cart.items.length === 0) {
      showToast('Carrinho vazio', 'warning');
      return;
    }

    setCheckoutLoading(true);
    try {
      const orderRequest = {
        pharmacyId: selectedPharmacy,
        items: cart.items.map(item => ({
          productVariantId: item.productVariantId,
          quantity: item.quantity
        })),
        notes: ''
      };

      await api.post('/orders', orderRequest);
      showToast('Pedido criado com sucesso!', 'success');
      await api.delete('/cart'); // Limpa o carrinho
      setTimeout(() => {
        navigate('/customer/orders');
      }, 1500);
    } catch (err: any) {
      console.error('Erro ao finalizar pedido:', err);
      showToast(err.response?.data?.message || 'Erro ao finalizar pedido', 'error');
    } finally {
      setCheckoutLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="cart">
        <LoadingSpinner message="Carregando carrinho..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="cart">
        <ErrorMessage message={error} onRetry={loadCart} />
      </div>
    );
  }

  if (!cart || !cart.items || cart.items.length === 0) {
    return (
      <div className="cart">
        <h2>Carrinho</h2>
        <div className="cart-empty">
          <p>Seu carrinho está vazio</p>
          <button onClick={() => navigate('/customer/products')} className="btn btn-primary">
            Ver Produtos
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="cart">
      <h2>Carrinho de Compras</h2>
      
      <div className="cart-items">
        {cart.items.map((item) => (
          <div key={item.productVariantId} className="cart-item">
            <div className="item-info">
              <h4>{item.productName}</h4>
              <p>{item.packageSize} - SKU: {item.productVariantSku}</p>
              {selectedPharmacy && getItemPrice(item.productVariantId) && (
                <p className="item-price">R$ {getItemPrice(item.productVariantId)!.toFixed(2)} cada</p>
              )}
            </div>
            <div className="item-actions">
              <div className="quantity-controls">
                <button onClick={() => updateQuantity(item.productVariantId, item.quantity - 1)}>-</button>
                <span>{item.quantity}</span>
                <button onClick={() => updateQuantity(item.productVariantId, item.quantity + 1)}>+</button>
              </div>
              <button onClick={() => removeItem(item.productVariantId)} className="btn-remove">
                Remover
              </button>
            </div>
          </div>
        ))}
      </div>

      <div className="cart-summary">
        <div className="pharmacy-selection">
          <label>Selecione a Farmácia:</label>
          <select 
            value={selectedPharmacy || ''} 
            onChange={(e) => setSelectedPharmacy(Number(e.target.value))}
            className="pharmacy-select"
          >
            <option value="">Selecione uma farmácia</option>
            {pharmacies.map((pharmacy) => (
              <option key={pharmacy.id} value={pharmacy.id}>
                {pharmacy.tradeName}
              </option>
            ))}
          </select>
        </div>

        <div className="cart-summary-total">
          {selectedPharmacy && (
            <div className="total-display">
              <strong>Total: R$ {calculateTotal().toFixed(2)}</strong>
            </div>
          )}
        </div>

        <div className="cart-actions">
          <button onClick={clearCart} className="btn btn-secondary">
            Limpar Carrinho
          </button>
          <button 
            onClick={handleCheckout} 
            className="btn btn-primary"
            disabled={!selectedPharmacy || checkoutLoading}
          >
            {checkoutLoading ? 'Processando...' : 'Finalizar Compra'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Cart;
