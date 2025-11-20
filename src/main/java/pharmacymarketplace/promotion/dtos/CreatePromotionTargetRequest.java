package pharmacymarketplace.promotion.dtos;

import jakarta.validation.constraints.NotNull;
import pharmacymarketplace.promotion.domain.jpa.PromotionTarget.TargetType;

public record CreatePromotionTargetRequest(
        @NotNull(message = "Tipo de alvo é obrigatório")
        TargetType targetType,

        Long targetId,

        Long productId,

        Long productVariantId,

        Long categoryId
) {}

