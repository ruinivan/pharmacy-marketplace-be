package pharmacymarketplace.promotion;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pharmacymarketplace.promotion.domain.jpa.Promotion;
import pharmacymarketplace.promotion.domain.jpa.PromotionRule;
import pharmacymarketplace.promotion.domain.jpa.PromotionTarget;
import pharmacymarketplace.promotion.dtos.PromotionDto;
import pharmacymarketplace.promotion.dtos.PromotionRuleDto;
import pharmacymarketplace.promotion.dtos.PromotionTargetDto;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    @Mapping(target = "pharmacyId", source = "pharmacy.id")
    @Mapping(target = "rules", expression = "java(mapRules(promotion.getRules()))")
    @Mapping(target = "targets", expression = "java(mapTargets(promotion.getTargets()))")
    PromotionDto toDto(Promotion promotion);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "categoryId", source = "category.id")
    PromotionTargetDto toTargetDto(PromotionTarget target);

    PromotionRuleDto toRuleDto(PromotionRule rule);

    default Set<PromotionRuleDto> mapRules(Set<PromotionRule> rules) {
        if (rules == null) return null;
        return rules.stream()
                .map(this::toRuleDto)
                .collect(Collectors.toSet());
    }

    default Set<PromotionTargetDto> mapTargets(Set<PromotionTarget> targets) {
        if (targets == null) return null;
        return targets.stream()
                .map(this::toTargetDto)
                .collect(Collectors.toSet());
    }
}

