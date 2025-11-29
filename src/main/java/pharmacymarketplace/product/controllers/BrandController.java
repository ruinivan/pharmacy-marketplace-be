package pharmacymarketplace.product.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.product.domain.jpa.Brand;
import pharmacymarketplace.product.repository.jpa.BrandRepository;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandRepository brandRepository;

    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok(brandRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada"));
        return ResponseEntity.ok(brand);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Brand> createBrand(@Valid @RequestBody Brand brand) {
        if (brandRepository.findByName(brand.getName()).isPresent()) {
            throw new AlreadyExistsException("Marca com este nome já existe");
        }
        Brand newBrand = brandRepository.save(brand);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBrand);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Brand> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody Brand brand
    ) {
        Brand existingBrand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada"));
        
        if (!existingBrand.getName().equals(brand.getName())) {
            if (brandRepository.findByName(brand.getName()).isPresent()) {
                throw new AlreadyExistsException("Marca com este nome já existe");
            }
        }
        
        existingBrand.setName(brand.getName());
        Brand updatedBrand = brandRepository.save(existingBrand);
        return ResponseEntity.ok(updatedBrand);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada"));
        brandRepository.delete(brand);
        return ResponseEntity.noContent().build();
    }
}

