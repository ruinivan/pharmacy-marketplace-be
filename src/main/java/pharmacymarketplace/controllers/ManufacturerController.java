package pharmacymarketplace.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.models.Manufacturer;
import pharmacymarketplace.services.ManufacturerService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerController {

    ManufacturerService service;

    public ManufacturerController(ManufacturerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer){
        Manufacturer manufacturerCreated = service.createManufacturer(manufacturer);
        return new ResponseEntity<>(manufacturerCreated, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ArrayList<Manufacturer>> getAllManufacturers(){
        ArrayList<Manufacturer> manufacturers = service.listAllManufacturers();
        if(manufacturers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(manufacturers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable long id){
        Manufacturer manufacturer = service.findManufacturerById(id);
        return new ResponseEntity<>(manufacturer, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> deleteManufacturerById(@PathVariable long id){
        service.deleteManufacturerById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
