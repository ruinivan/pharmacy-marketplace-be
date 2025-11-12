package pharmacymarketplace.services;

import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.models.Manufacturer;
import pharmacymarketplace.repositories.ManufacturerRepository;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class ManufacturerService {

    private final ManufacturerRepository repository;

    public ManufacturerService(ManufacturerRepository repository) {
        this.repository = repository;
    }

    public Manufacturer createManufacturer(Manufacturer manufacturer){
        repository.findByName(manufacturer.getName()).ifPresent(m -> {
            throw new AlreadyExistsException("Fabricante _" +  manufacturer.getName() + "_ já existe!");
        });
        return repository.save(manufacturer);
    }

    public ArrayList<Manufacturer> listAllManufacturers(){
       return (ArrayList<Manufacturer>) repository.findAll();
    }

    public Manufacturer findManufacturerById(long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fabricante com ID: " + id + " não encontrado!"));
    }

    public Manufacturer updateManufacturerById(long id, Manufacturer manufacturer){
        Manufacturer manufacturerUpdated = findManufacturerById(manufacturer.getId());
        manufacturerUpdated.setName(manufacturer.getName());

        repository.findByName(manufacturer.getName()).ifPresent(m -> {
            if(!Objects.equals(manufacturerUpdated.getName(), m.getName())){
                throw new AlreadyExistsException("Fabrica com nome _" + manufacturer.getName() + "_ já existe!");
            }
        });

        return repository.save(manufacturerUpdated);
    }

    public void deleteManufacturerById(long id){
        Manufacturer manufacturerDeleted = findManufacturerById(id);
        repository.delete(manufacturerDeleted);
    }
}
