import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import './Notifications.css';

interface Notification {
  id: string;
  userId: number;
  title: string;
  message: string;
  type: string;
  relatedEntityId?: number;
  read: boolean;
  createdAt: string;
}

const Notifications: React.FC = () => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [unreadCount, setUnreadCount] = useState<number>(0);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState<'ALL' | 'UNREAD'>('ALL');

  useEffect(() => {
    loadNotifications();
    loadUnreadCount();
    // Atualiza a cada 30 segundos
    const interval = setInterval(() => {
      loadNotifications();
      loadUnreadCount();
    }, 30000);
    return () => clearInterval(interval);
  }, []);

  const loadNotifications = async () => {
    try {
      const endpoint = filter === 'UNREAD' ? '/notifications/unread' : '/notifications';
      const response = await api.get(endpoint);
      setNotifications(response.data);
    } catch (err: any) {
      console.error('Erro ao carregar notificações:', err);
    } finally {
      setLoading(false);
    }
  };

  const loadUnreadCount = async () => {
    try {
      const response = await api.get('/notifications/unread/count');
      setUnreadCount(response.data);
    } catch (err: any) {
      console.error('Erro ao carregar contador:', err);
    }
  };

  const markAsRead = async (id: string) => {
    try {
      await api.patch(`/notifications/${id}/read`);
      loadNotifications();
      loadUnreadCount();
    } catch (err: any) {
      console.error('Erro ao marcar como lida:', err);
    }
  };

  const markAllAsRead = async () => {
    try {
      const unread = notifications.filter(n => !n.read);
      await Promise.all(unread.map(n => api.patch(`/notifications/${n.id}/read`)));
      loadNotifications();
      loadUnreadCount();
    } catch (err: any) {
      console.error('Erro ao marcar todas como lidas:', err);
    }
  };

  useEffect(() => {
    loadNotifications();
  }, [filter]);

  if (loading) return <div className="notifications"><p>Carregando notificações...</p></div>;

  return (
    <div className="notifications">
      <div className="notifications-header">
        <h2>Notificações</h2>
        {unreadCount > 0 && (
          <span className="unread-badge">{unreadCount} não lidas</span>
        )}
        <div className="notifications-actions">
          <select value={filter} onChange={(e) => setFilter(e.target.value as any)}>
            <option value="ALL">Todas</option>
            <option value="UNREAD">Não lidas</option>
          </select>
          {unreadCount > 0 && (
            <button onClick={markAllAsRead} className="btn btn-secondary btn-sm">
              Marcar todas como lidas
            </button>
          )}
        </div>
      </div>

      {notifications.length === 0 ? (
        <div className="no-notifications">
          <p>Nenhuma notificação</p>
        </div>
      ) : (
        <div className="notifications-list">
          {notifications.map((notification) => (
            <div 
              key={notification.id} 
              className={`notification-item ${notification.read ? 'read' : 'unread'}`}
              onClick={() => !notification.read && markAsRead(notification.id)}
            >
              <div className="notification-header">
                <h4>{notification.title}</h4>
                {!notification.read && <span className="unread-dot"></span>}
              </div>
              <p>{notification.message}</p>
              <span className="notification-date">
                {new Date(notification.createdAt).toLocaleString('pt-BR')}
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Notifications;
