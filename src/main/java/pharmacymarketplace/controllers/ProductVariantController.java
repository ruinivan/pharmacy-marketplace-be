// Em: /controllers/ProductVariantController.java
package pharmacymarketplace.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.models.ProductVariant;
import pharmacymarketplace.services.ProductVariantService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/products-variant")
public class ProductVariantController {

    private final ProductVariantService service;

    public ProductVariantController(ProductVariantService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductVariant> createProductVariant(@RequestBody ProductVariant productVariant) {
        ProductVariant newProductVariant = service.createProductVariant(productVariant);
        return new ResponseEntity<>(newProductVariant, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ArrayList<ProductVariant>> getAllProductVariant() {
        ArrayList<ProductVariant> productsVariant = service.listAllProductVariants();
        if (productsVariant.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productsVariant, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getProductVariantById(@PathVariable long id) {
        ProductVariant productVariant = service.findProductVariantById(id);
        return new ResponseEntity<>(productVariant, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVariant> updateProductVariantById(@PathVariable long id, @RequestBody ProductVariant productVariant) {
        ProductVariant productUpdated = service.updateProductVariantById(id, productVariant);
        return new ResponseEntity<>(productUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductVariantById(@PathVariable long id) {
        service.deleteProductVariantById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}