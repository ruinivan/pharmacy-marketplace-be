package pharmacymarketplace.review.domain.mongo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "reviews")
public record ReviewDocument(
        @Id
        String id,

        Long reviewerId, // FK para a 'users' (MySQL)

        int rating, // 1-5

        String comment,

        ReviewableType reviewableType, // Enum

        Long reviewableId, // FK para a entidade (MySQL)

        @CreatedDate
        Instant createdAt
) {
    public enum ReviewableType {
        PRODUCT_VARIANT,
        ORDER,
        DELIVERY,
        PHARMACY,
        USER,
        SYSTEM
    }
}