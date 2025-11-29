package pharmacymarketplace.inventory.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.inventory.dtos.InventoryDto;
import pharmacymarketplace.inventory.dtos.UpdateInventoryRequest;
import pharmacymarketplace.inventory.services.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<List<InventoryDto>> getInventoryByPharmacy(@PathVariable Long pharmacyId) {
        return ResponseEntity.ok(inventoryService.findByPharmacyId(pharmacyId));
    }

    @GetMapping("/product-variant/{productVariantId}")
    public ResponseEntity<List<InventoryDto>> getInventoryByProductVariant(@PathVariable Long productVariantId) {
        return ResponseEntity.ok(inventoryService.findByProductVariantId(productVariantId));
    }

    @PostMapping("/pharmacy/{pharmacyId}/product-variant/{productVariantId}")
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<InventoryDto> createInventory(
            @PathVariable Long pharmacyId,
            @PathVariable Long productVariantId,
            @Valid @RequestBody UpdateInventoryRequest request
    ) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(inventoryService.updateInventory(pharmacyId, productVariantId, request));
    }

    @PutMapping("/pharmacy/{pharmacyId}/product-variant/{productVariantId}")
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<InventoryDto> updateInventory(
            @PathVariable Long pharmacyId,
            @PathVariable Long productVariantId,
            @Valid @RequestBody UpdateInventoryRequest request
    ) {
        return ResponseEntity.ok(inventoryService.updateInventory(pharmacyId, productVariantId, request));
    }

    @DeleteMapping("/pharmacy/{pharmacyId}/product-variant/{productVariantId}")
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<Void> deleteInventory(
            @PathVariable Long pharmacyId,
            @PathVariable Long productVariantId
    ) {
        inventoryService.deleteInventory(pharmacyId, productVariantId);
        return ResponseEntity.noContent().build();
    }
}

