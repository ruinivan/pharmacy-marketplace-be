import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useAuth } from '../../contexts/AuthContext';
import { useToast } from '../../contexts/ToastContext';
import ProductForm from './ProductForm';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import Pagination from '../common/Pagination';
import './PharmacyProducts.css';

interface Product {
  publicId: string;
  name: string;
  description: string;
  activePrinciple: string;
  isPrescriptionRequired: boolean;
  variants: ProductVariant[];
}

interface ProductVariant {
  id: number;
  sku: string;
  dosage: string;
  packageSize: string;
}

interface Brand {
  id: number;
  name: string;
}

interface Manufacturer {
  id: number;
  name: string;
}

interface Category {
  id: number;
  name: string;
}

const PharmacyProducts: React.FC = () => {
  const { user } = useAuth();
  const { showToast } = useToast();
  const [products, setProducts] = useState<Product[]>([]);
  const [brands, setBrands] = useState<Brand[]>([]);
  const [manufacturers, setManufacturers] = useState<Manufacturer[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(12);

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      setLoading(true);
      const response = await api.get('/products');
      setProducts(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar produtos:', err);
      setError('Erro ao carregar produtos. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (publicId: string) => {
    if (!window.confirm('Deseja realmente excluir este produto?')) return;

    try {
      await api.delete(`/products/${publicId}`);
      showToast('Produto excluído com sucesso!', 'success');
      loadProducts();
    } catch (err: any) {
      console.error('Erro ao excluir produto:', err);
      showToast(err.response?.data?.message || 'Erro ao excluir produto', 'error');
    }
  };

  if (loading) {
    return (
      <div className="pharmacy-products">
        <LoadingSpinner message="Carregando produtos..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="pharmacy-products">
        <ErrorMessage message={error} onRetry={loadProducts} />
      </div>
    );
  }

  return (
    <div className="pharmacy-products">
      <div className="products-header">
        <div className="header-left">
          <h2>💊 Gerenciamento de Produtos</h2>
          <p className="products-count">Total de produtos cadastrados: <strong>{products.length}</strong></p>
        </div>
        <button 
          onClick={() => setShowCreateForm(true)} 
          className="btn btn-primary"
          title="Cadastrar novo produto"
        >
          ➕ Criar Novo Produto
        </button>
      </div>
      
      {products.length === 0 ? (
        <div className="no-products">
          <p>Nenhum produto cadastrado no sistema.</p>
          <p>Os produtos serão criados automaticamente pelo seed do sistema.</p>
        </div>
      ) : (
        <div className="products-grid">
          {products
            .slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage)
            .map((product) => (
            <div key={product.publicId} className="product-card">
              <div className="product-header">
                <h3>{product.name}</h3>
                {product.isPrescriptionRequired && (
                  <span className="prescription-badge">Requer Receita</span>
                )}
              </div>
              <p className="product-description">{product.description}</p>
              <p className="product-principle"><strong>Princípio Ativo:</strong> {product.activePrinciple}</p>
              <div className="variants">
                <strong>Variantes:</strong>
                <ul>
                  {product.variants?.map((variant) => (
                    <li key={variant.id}>
                      {variant.sku} - {variant.packageSize} ({variant.dosage})
                    </li>
                  ))}
                </ul>
              </div>
              <div className="product-actions">
                <button 
                  onClick={() => setEditingProduct(product)} 
                  className="btn btn-primary btn-sm"
                  title="Editar produto"
                >
                  ✏️ Editar
                </button>
                <button 
                  onClick={() => handleDelete(product.publicId)} 
                  className="btn btn-danger btn-sm"
                  title="Excluir produto"
                >
                  🗑️ Excluir
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {showCreateForm && (
        <ProductForm
          onClose={() => setShowCreateForm(false)}
          onSuccess={() => {
            setShowCreateForm(false);
            loadProducts();
          }}
        />
      )}

      {editingProduct && (
        <ProductForm
          product={editingProduct}
          onClose={() => setEditingProduct(null)}
          onSuccess={() => {
            setEditingProduct(null);
            loadProducts();
          }}
        />
      )}

      {products.length > itemsPerPage && (
        <Pagination
          currentPage={currentPage}
          totalPages={Math.ceil(products.length / itemsPerPage)}
          onPageChange={setCurrentPage}
          itemsPerPage={itemsPerPage}
          totalItems={products.length}
        />
      )}
    </div>
  );
};

export default PharmacyProducts;
