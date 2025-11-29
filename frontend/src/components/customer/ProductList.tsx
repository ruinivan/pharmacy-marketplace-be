import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import Pagination from '../common/Pagination';
import './ProductList.css';

interface Product {
  publicId: string;
  name: string;
  description: string;
  variants?: ProductVariant[] | Set<ProductVariant>;
  brandName?: string;
  manufacturerName?: string;
  isPrescriptionRequired?: boolean;
}

interface ProductVariant {
  id: number;
  sku: string;
  dosage: string;
  packageSize: string;
  price?: number;
}

interface InventoryItem {
  pharmacyId: number;
  productVariantId: number;
  quantity: number;
  price: number;
  productName: string;
  productVariantSku: string;
}

const ProductList: React.FC = () => {
  const navigate = useNavigate();
  const { showToast } = useToast();
  const [products, setProducts] = useState<Product[]>([]);
  const [inventory, setInventory] = useState<Map<number, InventoryItem[]>>(new Map());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(12);

  useEffect(() => {
    loadProducts();
    loadInventory();
  }, []);

  const loadProducts = async () => {
    try {
      setLoading(true);
      setError('');
      const response = await api.get('/products');
      console.log('Produtos recebidos:', response.data);
      // Garantir que variants seja um array
      const productsWithArrayVariants = response.data.map((product: Product) => ({
        ...product,
        variants: product.variants ? Array.from(product.variants) : []
      }));
      setProducts(productsWithArrayVariants);
      setCurrentPage(1); // Reset to first page when loading new data
    } catch (err: any) {
      console.error('Erro ao carregar produtos:', err);
      const errorMessage = err.response?.data?.message || err.message || 'Erro ao carregar produtos. Tente novamente.';
      setError(errorMessage);
      showToast(errorMessage, 'error');
    } finally {
      setLoading(false);
    }
  };

  const loadInventory = async () => {
    try {
      const pharmaciesResponse = await api.get('/pharmacies');
      const pharmacies = pharmaciesResponse.data;
      
      const inventoryMap = new Map<number, InventoryItem[]>();
      for (const pharmacy of pharmacies) {
        try {
          const invResponse = await api.get(`/inventory/pharmacy/${pharmacy.id}`);
          inventoryMap.set(pharmacy.id, invResponse.data);
        } catch (err) {
          console.error(`Erro ao carregar inventário da farmácia ${pharmacy.id}:`, err);
        }
      }
      setInventory(inventoryMap);
    } catch (error) {
      console.error('Erro ao carregar inventário:', error);
    }
  };

  const addToCart = async (productVariantId: number) => {
    try {
      await api.post('/cart/items', {
        productVariantId,
        quantity: 1
      });
      showToast('Produto adicionado ao carrinho!', 'success');
    } catch (err: any) {
      console.error('Erro ao adicionar ao carrinho:', err);
      showToast(err.response?.data?.message || 'Erro ao adicionar ao carrinho', 'error');
    }
  };

  const addToFavorites = async (productVariantId: number) => {
    try {
      await api.post('/favorites', {
        favoriteType: 'PRODUCT',
        productVariantId
      });
      showToast('Produto adicionado aos favoritos!', 'success');
    } catch (err: any) {
      console.error('Erro ao adicionar aos favoritos:', err);
      const errorMsg = err.response?.data?.message || 'Erro ao adicionar aos favoritos';
      if (errorMsg.includes('já está nos favoritos')) {
        showToast('Produto já está nos favoritos', 'info');
      } else {
        showToast(errorMsg, 'error');
      }
    }
  };

  const getPriceForVariant = (variantId: number): { price: number; pharmacy: string } | null => {
    const inventoryArray = Array.from(inventory.entries());
    for (const [pharmacyId, items] of inventoryArray) {
      const item = items.find((i: InventoryItem) => i.productVariantId === variantId);
      if (item && item.quantity > 0) {
        return { price: item.price, pharmacy: `Farmácia ${pharmacyId}` };
      }
    }
    return null;
  };

  const filteredProducts = products.filter(product =>
    product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    product.description?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const paginatedProducts = filteredProducts.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  useEffect(() => {
    setCurrentPage(1); // Reset page when search term changes
  }, [searchTerm]);

  if (loading) {
    return (
      <div className="product-list">
        <LoadingSpinner message="Carregando produtos..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="product-list">
        <ErrorMessage message={error} onRetry={loadProducts} />
      </div>
    );
  }

  return (
    <div className="product-list">
      <div className="product-list-header">
        <h2>Produtos Disponíveis</h2>
        <div className="product-list-info">
          <span className="product-count">
            {filteredProducts.length} {filteredProducts.length === 1 ? 'produto encontrado' : 'produtos encontrados'}
          </span>
          <button 
            onClick={loadProducts} 
            className="btn btn-secondary btn-sm"
            title="Atualizar lista"
          >
            🔄 Atualizar
          </button>
        </div>
      </div>
      <input
        type="text"
        placeholder="Buscar produtos por nome ou descrição..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="search-input"
      />
      <div className="products-grid">
        {paginatedProducts.length === 0 ? (
          <div className="no-results">
            <p>Nenhum produto encontrado</p>
            {searchTerm && (
              <button onClick={() => setSearchTerm('')} className="btn btn-secondary">
                Limpar Busca
              </button>
            )}
          </div>
        ) : (
          paginatedProducts.map((product) => (
            <div key={product.publicId} className="product-card">
              <div className="product-card-header">
                <h3>{product.name}</h3>
                {product.isPrescriptionRequired && (
                  <span className="prescription-badge">📋 Requer Receita</span>
                )}
              </div>
              <p className="product-description">{product.description}</p>
              {(product.brandName || product.manufacturerName) && (
                <div className="product-meta">
                  {product.brandName && <span className="product-brand">Marca: {product.brandName}</span>}
                  {product.manufacturerName && <span className="product-manufacturer">Fabricante: {product.manufacturerName}</span>}
                </div>
              )}
              {product.variants && Array.isArray(product.variants) && product.variants.length > 0 && (
                <div className="variants">
                  {product.variants.map((variant) => {
                    const priceInfo = getPriceForVariant(variant.id);
                    return (
                      <div key={variant.id} className="variant">
                        <div className="variant-info">
                          <span className="variant-size">{variant.packageSize}</span>
                          {priceInfo && (
                            <span className="variant-price">R$ {priceInfo.price.toFixed(2)}</span>
                          )}
                        </div>
                        <div className="variant-actions">
                          <button 
                            onClick={() => addToCart(variant.id)}
                            className="btn-add-cart"
                            disabled={!priceInfo}
                          >
                            {priceInfo ? 'Adicionar ao Carrinho' : 'Indisponível'}
                          </button>
                          <button 
                            onClick={() => addToFavorites(variant.id)}
                            className="btn-favorite"
                            title="Adicionar aos favoritos"
                          >
                            ♥
                          </button>
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
          ))
        )}
      </div>

      {filteredProducts.length > itemsPerPage && (
        <Pagination
          currentPage={currentPage}
          totalPages={Math.ceil(filteredProducts.length / itemsPerPage)}
          onPageChange={setCurrentPage}
          itemsPerPage={itemsPerPage}
          totalItems={filteredProducts.length}
        />
      )}
    </div>
  );
};

export default ProductList;
