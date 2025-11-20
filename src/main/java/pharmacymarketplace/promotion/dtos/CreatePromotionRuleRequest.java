package pharmacymarketplace.promotion.dtos;

import jakarta.validation.constraints.NotNull;
import pharmacymarketplace.promotion.domain.jpa.PromotionRule.RuleType;

import java.math.BigDecimal;

public record CreatePromotionRuleRequest(
        @NotNull(message = "Tipo de regra é obrigatório")
        RuleType ruleType,

        String ruleValue,

        BigDecimal minPurchaseAmount,

        Integer minQuantity
) {}

