package pharmacymarketplace.review;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pharmacymarketplace.review.domain.mongo.ReviewDocument;
import pharmacymarketplace.review.dtos.ReviewDto;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    
    @Mapping(target = "reviewerName", ignore = true)
    ReviewDto toDto(ReviewDocument review);
    
    default ReviewDto toDtoWithReviewerName(ReviewDocument review, String reviewerName) {
        return new ReviewDto(
                review.id(),
                review.reviewerId(),
                reviewerName,
                review.rating(),
                review.comment(),
                review.reviewableType(),
                review.reviewableId(),
                review.createdAt()
        );
    }
}

