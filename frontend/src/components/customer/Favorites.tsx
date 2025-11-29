import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import './Favorites.css';

interface Favorite {
  id: number;
  favoriteType: 'PRODUCT' | 'PHARMACY';
  productVariantId?: number;
  productName?: string;
  pharmacyId?: number;
  pharmacyName?: string;
}

const Favorites: React.FC = () => {
  const { showToast } = useToast();
  const [favorites, setFavorites] = useState<Favorite[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterType, setFilterType] = useState<'ALL' | 'PRODUCT' | 'PHARMACY'>('ALL');

  useEffect(() => {
    loadFavorites();
  }, []);

  const loadFavorites = async () => {
    try {
      const response = await api.get('/favorites');
      setFavorites(response.data);
      setError('');
    } catch (err: any) {
      console.error('Erro ao carregar favoritos:', err);
      setError('Erro ao carregar favoritos');
    } finally {
      setLoading(false);
    }
  };

  const removeFavorite = async (id: number) => {
    try {
      await api.delete(`/favorites/${id}`);
      showToast('Favorito removido!', 'success');
      loadFavorites();
    } catch (err: any) {
      console.error('Erro ao remover favorito:', err);
      showToast('Erro ao remover favorito', 'error');
    }
  };

  const removeProductFavorite = async (productVariantId: number) => {
    try {
      await api.delete(`/favorites/product/${productVariantId}`);
      showToast('Produto removido dos favoritos!', 'success');
      loadFavorites();
    } catch (err: any) {
      console.error('Erro ao remover favorito:', err);
      showToast('Erro ao remover favorito', 'error');
    }
  };

  const removePharmacyFavorite = async (pharmacyId: number) => {
    try {
      await api.delete(`/favorites/pharmacy/${pharmacyId}`);
      showToast('Farmácia removida dos favoritos!', 'success');
      loadFavorites();
    } catch (err: any) {
      console.error('Erro ao remover favorito:', err);
      showToast('Erro ao remover favorito', 'error');
    }
  };

  const filteredFavorites = filterType === 'ALL'
    ? favorites
    : favorites.filter(fav => fav.favoriteType === filterType);

  if (loading) {
    return (
      <div className="favorites">
        <LoadingSpinner message="Carregando favoritos..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="favorites">
        <ErrorMessage message={error} onRetry={loadFavorites} />
      </div>
    );
  }

  return (
    <div className="favorites">
      <h2>Meus Favoritos</h2>
      
      <div className="filter-section">
        <label>Filtrar por Tipo:</label>
        <select value={filterType} onChange={(e) => setFilterType(e.target.value as any)}>
          <option value="ALL">Todos</option>
          <option value="PRODUCT">Produtos</option>
          <option value="PHARMACY">Farmácias</option>
        </select>
      </div>

      {filteredFavorites.length === 0 ? (
        <div className="no-favorites">
          <p>Nenhum favorito encontrado</p>
        </div>
      ) : (
        <div className="favorites-list">
          {filteredFavorites.map((favorite) => (
            <div key={favorite.id} className="favorite-card">
              {favorite.favoriteType === 'PRODUCT' && favorite.productName && (
                <>
                  <div className="favorite-info">
                    <h3>{favorite.productName}</h3>
                    <span className="favorite-type">Produto</span>
                  </div>
                  <button 
                    onClick={() => favorite.productVariantId && removeProductFavorite(favorite.productVariantId)}
                    className="btn btn-danger btn-sm"
                  >
                    Remover
                  </button>
                </>
              )}
              {favorite.favoriteType === 'PHARMACY' && favorite.pharmacyName && (
                <>
                  <div className="favorite-info">
                    <h3>{favorite.pharmacyName}</h3>
                    <span className="favorite-type">Farmácia</span>
                  </div>
                  <button 
                    onClick={() => favorite.pharmacyId && removePharmacyFavorite(favorite.pharmacyId)}
                    className="btn btn-danger btn-sm"
                  >
                    Remover
                  </button>
                </>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Favorites;
