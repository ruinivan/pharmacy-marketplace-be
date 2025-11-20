package pharmacymarketplace.review.dtos;

import pharmacymarketplace.review.domain.mongo.ReviewDocument.ReviewableType;

import java.time.Instant;

public record ReviewDto(
        String id,
        Long reviewerId,
        String reviewerName,
        int rating,
        String comment,
        ReviewableType reviewableType,
        Long reviewableId,
        Instant createdAt
) {}

