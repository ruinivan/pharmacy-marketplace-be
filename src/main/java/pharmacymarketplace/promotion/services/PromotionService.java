package pharmacymarketplace.promotion.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.pharmacy.repository.jpa.PharmacyRepository;
import pharmacymarketplace.promotion.PromotionMapper;
import pharmacymarketplace.promotion.domain.jpa.Promotion;
import pharmacymarketplace.promotion.domain.jpa.PromotionRule;
import pharmacymarketplace.promotion.domain.jpa.PromotionTarget;
import pharmacymarketplace.promotion.dtos.CreatePromotionRequest;
import pharmacymarketplace.promotion.dtos.PromotionDto;
import pharmacymarketplace.promotion.repository.jpa.PromotionRepository;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PromotionMapper promotionMapper;

    public PromotionDto findById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoção não encontrada"));
        return promotionMapper.toDto(promotion);
    }

    public List<PromotionDto> findByPharmacyId(Long pharmacyId) {
        return promotionRepository.findByPharmacyId(pharmacyId).stream()
                .map(promotionMapper::toDto)
                .toList();
    }

    public List<PromotionDto> findActivePromotions(Long pharmacyId) {
        Instant now = Instant.now();
        return promotionRepository
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActive(now, now, true)
                .stream()
                .filter(p -> p.getPharmacy().getId().equals(pharmacyId))
                .map(promotionMapper::toDto)
                .toList();
    }

    @Transactional
    public PromotionDto createPromotion(CreatePromotionRequest request) {
        Pharmacy pharmacy = pharmacyRepository.findById(request.pharmacyId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmácia não encontrada"));

        Promotion promotion = new Promotion();
        promotion.setName(request.name());
        promotion.setDescription(request.description());
        promotion.setDiscountType(request.discountType());
        promotion.setDiscountValue(request.discountValue());
        promotion.setStartDate(request.startDate());
        promotion.setEndDate(request.endDate());
        promotion.setIsActive(request.isActive() != null ? request.isActive() : true);
        promotion.setMaxUses(request.maxUses());
        promotion.setCurrentUses(0);
        promotion.setPharmacy(pharmacy);

        Set<PromotionRule> rules = new HashSet<>();
        if (request.rules() != null) {
            for (var ruleRequest : request.rules()) {
                PromotionRule rule = new PromotionRule();
                rule.setPromotion(promotion);
                rule.setRuleType(ruleRequest.ruleType());
                rule.setRuleValue(ruleRequest.ruleValue());
                rule.setMinPurchaseAmount(ruleRequest.minPurchaseAmount());
                rule.setMinQuantity(ruleRequest.minQuantity());
                rules.add(rule);
            }
        }
        promotion.setRules(rules);

        Set<PromotionTarget> targets = new HashSet<>();
        if (request.targets() != null) {
            for (var targetRequest : request.targets()) {
                PromotionTarget target = new PromotionTarget();
                target.setPromotion(promotion);
                target.setTargetType(targetRequest.targetType());
                target.setTargetId(targetRequest.targetId());
                // TODO: Set product, productVariant, category based on targetType
                targets.add(target);
            }
        }
        promotion.setTargets(targets);

        Promotion saved = promotionRepository.save(promotion);
        return promotionMapper.toDto(saved);
    }

    @Transactional
    public PromotionDto updatePromotion(Long id, CreatePromotionRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoção não encontrada"));

        promotion.setName(request.name());
        promotion.setDescription(request.description());
        promotion.setDiscountType(request.discountType());
        promotion.setDiscountValue(request.discountValue());
        promotion.setStartDate(request.startDate());
        promotion.setEndDate(request.endDate());
        promotion.setIsActive(request.isActive() != null ? request.isActive() : promotion.getIsActive());
        promotion.setMaxUses(request.maxUses());

        return promotionMapper.toDto(promotionRepository.save(promotion));
    }

    @Transactional
    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoção não encontrada"));
        promotionRepository.delete(promotion);
    }
}

