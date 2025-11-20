package pharmacymarketplace.promotion.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pharmacymarketplace.promotion.domain.jpa.Promotion.DiscountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record CreatePromotionRequest(
        @NotBlank(message = "Nome da promoção é obrigatório")
        String name,

        String description,

        @NotNull(message = "Tipo de desconto é obrigatório")
        DiscountType discountType,

        @NotNull(message = "Valor do desconto é obrigatório")
        BigDecimal discountValue,

        @NotNull(message = "Data de início é obrigatória")
        Instant startDate,

        @NotNull(message = "Data de término é obrigatória")
        Instant endDate,

        Boolean isActive,

        Integer maxUses,

        @NotNull(message = "ID da farmácia é obrigatório")
        Long pharmacyId,

        Set<CreatePromotionRuleRequest> rules,

        Set<CreatePromotionTargetRequest> targets
) {}

