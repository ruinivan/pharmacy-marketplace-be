package pharmacymarketplace.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.model.Brand;
import pharmacymarketplace.services.BrandService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService service;

    public BrandController(BrandService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand){
        return new ResponseEntity<>(service.createBrand(brand), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ArrayList<Brand>> getAllBrands() {
        return new ResponseEntity<>(service.listAllBrand(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable long id) {
        return new ResponseEntity<>(service.findBrandById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrandById(@PathVariable long id, @RequestBody Brand brand) {
        Brand brandUpdated = service.updateBrandById(id, brand);
        return new ResponseEntity<>(brandUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Brand> deleteBrandById(@PathVariable long id) {
        Brand brandDeleted = service.findBrandById(id);
        return new ResponseEntity<>(brandDeleted, HttpStatus.OK);
    }
}
