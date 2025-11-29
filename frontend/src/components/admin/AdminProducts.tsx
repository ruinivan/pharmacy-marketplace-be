import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import ProductForm from '../pharmacy/ProductForm';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import Pagination from '../common/Pagination';
import './AdminProducts.css';

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

const AdminProducts: React.FC = () => {
  const { showToast } = useToast();
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(12);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      const response = await api.get('/products');
      setProducts(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar produtos:', err);
      setError('Erro ao carregar produtos');
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

  const paginatedProducts = products.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  if (loading) {
    return (
      <div className="admin-products">
        <LoadingSpinner message="Carregando produtos..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="admin-products">
        <ErrorMessage message={error} onRetry={loadProducts} />
      </div>
    );
  }

  return (
    <div className="admin-products">
      <div className="products-header">
        <h2>Gerenciamento de Produtos</h2>
        <button 
          onClick={() => {
            setEditingProduct(null);
            setShowCreateForm(true);
          }} 
          className="btn btn-primary"
        >
          Criar Novo Produto
        </button>
      </div>
      <p>Total de produtos: {products.length}</p>
      
      {products.length === 0 ? (
        <div className="no-products">
          <p>Nenhum produto encontrado</p>
        </div>
      ) : (
        <>
          <div className="products-list">
            {paginatedProducts.map((product) => (
            <div key={product.publicId} className="product-card">
              <div className="product-header">
                <h3>{product.name}</h3>
                {product.isPrescriptionRequired && (
                  <span className="prescription-badge">Requer Receita</span>
                )}
              </div>
              <p>{product.description}</p>
              <p><strong>Princípio Ativo:</strong> {product.activePrinciple}</p>
              <div className="variants">
                <strong>Variantes:</strong> {product.variants?.length || 0}
              </div>
              <div className="product-actions">
                <button 
                  onClick={() => {
                    setEditingProduct(product);
                    setShowCreateForm(true);
                  }} 
                  className="btn btn-primary btn-sm"
                >
                  Editar
                </button>
                <button 
                  onClick={() => handleDelete(product.publicId)} 
                  className="btn btn-danger btn-sm"
                >
                  Excluir
                </button>
              </div>
            </div>
          ))}
          </div>

          {products.length > itemsPerPage && (
            <Pagination
              currentPage={currentPage}
              totalPages={Math.ceil(products.length / itemsPerPage)}
              onPageChange={setCurrentPage}
              itemsPerPage={itemsPerPage}
              totalItems={products.length}
            />
          )}
        </>
      )}

      {showCreateForm && (
        <ProductForm
          product={editingProduct || undefined}
          onClose={() => {
            setShowCreateForm(false);
            setEditingProduct(null);
          }}
          onSuccess={() => {
            setShowCreateForm(false);
            setEditingProduct(null);
            loadProducts();
          }}
        />
      )}
    </div>
  );
};

export default AdminProducts;
