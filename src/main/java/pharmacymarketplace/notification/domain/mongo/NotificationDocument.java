package pharmacymarketplace.notification.domain.mongo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notifications")
public record NotificationDocument(
        @Id
        String id,
        
        Long userId,
        
        String title,
        
        String message,
        
        NotificationType type,
        
        Long relatedEntityId,
        
        boolean read,
        
        @CreatedDate
        Instant createdAt
) {
    public enum NotificationType {
        ORDER_CREATED,
        ORDER_STATUS_CHANGED,
        ORDER_READY_FOR_PICKUP,
        DELIVERY_ASSIGNED,
        DELIVERY_IN_TRANSIT,
        DELIVERY_DELIVERED,
        SYSTEM
    }
}

