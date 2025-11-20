package pharmacymarketplace.promotion.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.promotion.dtos.CreatePromotionRequest;
import pharmacymarketplace.promotion.dtos.PromotionDto;
import pharmacymarketplace.promotion.services.PromotionService;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<List<PromotionDto>> getPromotionsByPharmacy(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(promotionService.findByPharmacyId(pharmacyId));
    }

    @GetMapping("/pharmacy/{pharmacyId}/active")
    public ResponseEntity<List<PromotionDto>> getActivePromotions(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(promotionService.findActivePromotions(pharmacyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDto> getPromotionById(@PathVariable Long id) {
        return ResponseEntity.ok(promotionService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<PromotionDto> createPromotion(
            @Valid @RequestBody CreatePromotionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionService.createPromotion(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<PromotionDto> updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody CreatePromotionRequest request
    ) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }
}

