package pharmacymarketplace.notification.dtos;

import pharmacymarketplace.notification.domain.mongo.NotificationDocument.NotificationType;
import java.time.Instant;

public record NotificationDto(
        String id,
        Long userId,
        String title,
        String message,
        NotificationType type,
        Long relatedEntityId,
        boolean read,
        Instant createdAt
) {}

