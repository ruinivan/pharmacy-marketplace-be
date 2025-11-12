package pharmacymarketplace.product.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.product.domain.jpa.Product;

import java.util.UUID;

//...product/web/ProductController.java
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService; // Assumindo que existe
    private final ProductMapper productMapper; // Assumindo que existe

    @GetMapping("/{publicId}")
    public ResponseEntity<ProductDto> getProductByPublicId(@PathVariable UUID publicId) {
        Product product = productService.findByPublicId(publicId);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')") // [34, 89, 90]
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        Product newProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDto(newProduct));
    }
}