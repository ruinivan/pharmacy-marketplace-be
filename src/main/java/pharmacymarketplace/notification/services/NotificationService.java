package pharmacymarketplace.notification.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.notification.domain.mongo.NotificationDocument;
import pharmacymarketplace.notification.dtos.NotificationDto;
import pharmacymarketplace.notification.repository.mongo.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(Long userId, String title, String message, 
                                   NotificationDocument.NotificationType type, Long relatedEntityId) {
        NotificationDocument notification = new NotificationDocument(
                null,
                userId,
                title,
                message,
                type,
                relatedEntityId,
                false,
                null
        );
        notificationRepository.save(notification);
    }

    public List<NotificationDto> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    public List<NotificationDto> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    public void markAsRead(String notificationId, Long userId) {
        NotificationDocument notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada"));
        if (!notification.userId().equals(userId)) {
            throw new IllegalArgumentException("Notificação não pertence ao usuário");
        }
        NotificationDocument updated = new NotificationDocument(
                notification.id(),
                notification.userId(),
                notification.title(),
                notification.message(),
                notification.type(),
                notification.relatedEntityId(),
                true,
                notification.createdAt()
        );
        notificationRepository.save(updated);
    }

    private NotificationDto toDto(NotificationDocument doc) {
        return new NotificationDto(
                doc.id(),
                doc.userId(),
                doc.title(),
                doc.message(),
                doc.type(),
                doc.relatedEntityId(),
                doc.read(),
                doc.createdAt()
        );
    }
}

