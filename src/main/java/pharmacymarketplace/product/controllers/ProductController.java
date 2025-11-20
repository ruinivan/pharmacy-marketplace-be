package pharmacymarketplace.product.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.product.ProductMapper;
import pharmacymarketplace.product.domain.jpa.Product;
import pharmacymarketplace.product.dtos.CreateProductRequest;
import pharmacymarketplace.product.dtos.ProductDto;
import pharmacymarketplace.product.services.ProductService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<ProductDto> getProductByPublicId(@PathVariable UUID publicId) {
        Product product = productService.findByPublicId(publicId);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String name) {
        List<ProductDto> products = productService.searchByName(name).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        Product newProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDto(newProduct));
    }

    @PutMapping("/{publicId}")
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable UUID publicId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        Product updatedProduct = productService.updateProduct(publicId, request);
        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }

    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasRole('ROLE_PHARMACY_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID publicId) {
        productService.deleteProduct(publicId);
        return ResponseEntity.noContent().build();
    }
}