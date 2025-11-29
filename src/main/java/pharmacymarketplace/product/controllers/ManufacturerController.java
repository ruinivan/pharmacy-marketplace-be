package pharmacymarketplace.product.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.product.domain.jpa.Manufacturer;
import pharmacymarketplace.product.repository.jpa.ManufacturerRepository;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerRepository manufacturerRepository;

    @GetMapping
    public ResponseEntity<List<Manufacturer>> getAllManufacturers() {
        return ResponseEntity.ok(manufacturerRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado"));
        return ResponseEntity.ok(manufacturer);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Manufacturer> createManufacturer(@Valid @RequestBody Manufacturer manufacturer) {
        if (manufacturerRepository.findByName(manufacturer.getName()).isPresent()) {
            throw new AlreadyExistsException("Fabricante com este nome já existe");
        }
        Manufacturer newManufacturer = manufacturerRepository.save(manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(newManufacturer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Manufacturer> updateManufacturer(
            @PathVariable Long id,
            @Valid @RequestBody Manufacturer manufacturer
    ) {
        Manufacturer existingManufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado"));
        
        if (!existingManufacturer.getName().equals(manufacturer.getName())) {
            if (manufacturerRepository.findByName(manufacturer.getName()).isPresent()) {
                throw new AlreadyExistsException("Fabricante com este nome já existe");
            }
        }
        
        existingManufacturer.setName(manufacturer.getName());
        Manufacturer updatedManufacturer = manufacturerRepository.save(existingManufacturer);
        return ResponseEntity.ok(updatedManufacturer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado"));
        manufacturerRepository.delete(manufacturer);
        return ResponseEntity.noContent().build();
    }
}

