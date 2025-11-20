package pharmacymarketplace.promotion.dtos;

import pharmacymarketplace.promotion.domain.jpa.PromotionTarget.TargetType;

public record PromotionTargetDto(
        Long id,
        TargetType targetType,
        Long targetId,
        Long productId,
        Long productVariantId,
        Long categoryId
) {}

