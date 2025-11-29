package pharmacymarketplace.notification.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pharmacymarketplace.notification.domain.mongo.NotificationDocument;

import java.util.List;

public interface NotificationRepository extends MongoRepository<NotificationDocument, String> {
    List<NotificationDocument> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<NotificationDocument> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);
    long countByUserIdAndReadFalse(Long userId);
}

