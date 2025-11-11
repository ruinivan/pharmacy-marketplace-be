// Em: /controllers/ProductController.java
package pharmacymarketplace.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.model.Product;
import pharmacymarketplace.services.ProductService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product newProduct = service.createProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ArrayList<Product>> getAllProduct() {
        ArrayList<Product> products = service.listAllProducts();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        Product product = service.findProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> putProductById(@PathVariable long id, @RequestBody Product product) {
        Product productUpdated = service.updateProductById(id, product);
        return new ResponseEntity<>(productUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable long id) {
        service.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}