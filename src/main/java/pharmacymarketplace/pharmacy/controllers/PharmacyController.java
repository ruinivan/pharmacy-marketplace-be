package pharmacymarketplace.pharmacy.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.pharmacy.dtos.CreatePharmacyRequest;
import pharmacymarketplace.pharmacy.dtos.PharmacyDto;
import pharmacymarketplace.pharmacy.services.PharmacyService;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacies")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping
    public ResponseEntity<List<PharmacyDto>> getAllPharmacies() {
        return ResponseEntity.ok(pharmacyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyDto> getPharmacyById(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PharmacyDto> createPharmacy(
            @Valid @RequestBody CreatePharmacyRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.createPharmacy(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_PHARMACY_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<PharmacyDto> updatePharmacy(
            @PathVariable Long id,
            @Valid @RequestBody CreatePharmacyRequest request
    ) {
        return ResponseEntity.ok(pharmacyService.updatePharmacy(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deletePharmacy(@PathVariable Long id) {
        pharmacyService.deletePharmacy(id);
        return ResponseEntity.noContent().build();
    }
}

