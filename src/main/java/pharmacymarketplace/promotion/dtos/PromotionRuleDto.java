package pharmacymarketplace.promotion.dtos;

import pharmacymarketplace.promotion.domain.jpa.PromotionRule.RuleType;

import java.math.BigDecimal;

public record PromotionRuleDto(
        Long id,
        RuleType ruleType,
        String ruleValue,
        BigDecimal minPurchaseAmount,
        Integer minQuantity
) {}

