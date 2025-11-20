package pharmacymarketplace.review;

import org.mapstruct.Mapper;
import pharmacymarketplace.review.domain.mongo.ReviewDocument;
import pharmacymarketplace.review.dtos.ReviewDto;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDto toDto(ReviewDocument review);
}

