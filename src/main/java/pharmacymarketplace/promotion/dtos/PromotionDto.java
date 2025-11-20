package pharmacymarketplace.promotion.dtos;

import pharmacymarketplace.promotion.domain.jpa.Promotion.DiscountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record PromotionDto(
        Long id,
        String name,
        String description,
        DiscountType discountType,
        BigDecimal discountValue,
        Instant startDate,
        Instant endDate,
        Boolean isActive,
        Integer maxUses,
        Integer currentUses,
        Long pharmacyId,
        Set<PromotionRuleDto> rules,
        Set<PromotionTargetDto> targets
) {}

