package pharmacymarketplace.review.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pharmacymarketplace.review.domain.mongo.ReviewDocument;

import java.util.List;

public interface ReviewRepository extends MongoRepository<ReviewDocument, String> {
    List<ReviewDocument> findByReviewableTypeAndReviewableId(
            ReviewDocument.ReviewableType reviewableType, Long reviewableId);
    List<ReviewDocument> findByReviewerId(Long reviewerId);
    List<ReviewDocument> findByRating(int rating);
    List<ReviewDocument> findByReviewableType(ReviewDocument.ReviewableType reviewableType);
}

