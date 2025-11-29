import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { useToast } from '../../contexts/ToastContext';
import './Reviews.css';

interface Review {
  id: string;
  reviewerId: number;
  reviewerName: string;
  rating: number;
  comment?: string;
  reviewableType: 'PRODUCT' | 'PHARMACY';
  reviewableId: number;
  createdAt: string;
}

interface ReviewProps {
  reviewableType: 'PRODUCT' | 'PHARMACY';
  reviewableId: number;
}

const Reviews: React.FC<ReviewProps> = ({ reviewableType, reviewableId }) => {
  const { showToast } = useToast();
  const [reviews, setReviews] = useState<Review[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [rating, setRating] = useState<number>(5);
  const [comment, setComment] = useState('');

  useEffect(() => {
    loadReviews();
  }, [reviewableType, reviewableId]);

  const loadReviews = async () => {
    try {
      const response = await api.get(`/reviews?reviewableType=${reviewableType}&reviewableId=${reviewableId}`);
      setReviews(response.data);
    } catch (err: any) {
      console.error('Erro ao carregar avaliações:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/reviews', {
        rating,
        comment,
        reviewableType,
        reviewableId
      });
      showToast('Avaliação enviada com sucesso!', 'success');
      setShowForm(false);
      setRating(5);
      setComment('');
      loadReviews();
    } catch (err: any) {
      console.error('Erro ao enviar avaliação:', err);
      showToast(err.response?.data?.message || 'Erro ao enviar avaliação', 'error');
    }
  };

  const handleDelete = async (id: string) => {
    if (!window.confirm('Deseja realmente excluir esta avaliação?')) return;

    try {
      await api.delete(`/reviews/${id}`);
      showToast('Avaliação excluída!', 'success');
      loadReviews();
    } catch (err: any) {
      console.error('Erro ao excluir avaliação:', err);
      showToast('Erro ao excluir avaliação', 'error');
    }
  };

  const averageRating = reviews.length > 0
    ? reviews.reduce((sum, r) => sum + r.rating, 0) / reviews.length
    : 0;

  if (loading) return <div className="reviews"><p>Carregando avaliações...</p></div>;

  return (
    <div className="reviews">
      <div className="reviews-header">
        <h3>Avaliações</h3>
        <div className="rating-summary">
          <span className="average-rating">{averageRating.toFixed(1)}</span>
          <span className="total-reviews">({reviews.length} avaliações)</span>
        </div>
        <button onClick={() => setShowForm(true)} className="btn btn-primary btn-sm">
          Avaliar
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="review-form">
          <div className="form-group">
            <label>Avaliação (1-5):</label>
            <input
              type="number"
              min="1"
              max="5"
              value={rating}
              onChange={(e) => setRating(Number(e.target.value))}
              required
            />
          </div>
          <div className="form-group">
            <label>Comentário:</label>
            <textarea
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              rows={3}
            />
          </div>
          <div className="form-actions">
            <button type="submit" className="btn btn-primary">Enviar</button>
            <button type="button" onClick={() => setShowForm(false)} className="btn btn-secondary">
              Cancelar
            </button>
          </div>
        </form>
      )}

      <div className="reviews-list">
        {reviews.length === 0 ? (
          <p>Nenhuma avaliação ainda</p>
        ) : (
          reviews.map((review) => (
            <div key={review.id} className="review-item">
              <div className="review-header">
                <span className="reviewer-name">{review.reviewerName}</span>
                <span className="review-rating">{'★'.repeat(review.rating)}{'☆'.repeat(5 - review.rating)}</span>
                <span className="review-date">{new Date(review.createdAt).toLocaleDateString('pt-BR')}</span>
              </div>
              {review.comment && <p className="review-comment">{review.comment}</p>}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default Reviews;

