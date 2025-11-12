// Em: /services/BrandService.java
package pharmacymarketplace.services;

import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.models.Brand;
import pharmacymarketplace.repositories.BrandRepository;
import pharmacymarketplace.exceptions.ResourceNotFoundException; // Importe

import java.util.ArrayList;
import java.util.Objects;

@Service
public class BrandService {

    private final BrandRepository repository;

    // Injeção por construtor (como falamos antes)
    public BrandService(BrandRepository repository) {
        this.repository = repository;
    }

    public Brand createBrand(Brand brand) {
        // A lógica de "já existe" continua igual, lançando a exceção
        repository.findByName(brand.getName()).ifPresent(b -> {
            throw new AlreadyExistsException("Usuário com email _" + brand.getName() + "_ já existe!");
        });

        return repository.save(brand);
    }

    public ArrayList<Brand> listAllBrand() {
        return (ArrayList<Brand>) repository.findAll();
    }

    // MUDANÇA: Não retorna mais Optional<Brand>
    public Brand findBrandById(long id) {
        // Encontra ou lança a exceção 404
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca com ID: " + id + " não encontrado!"));
    }

    // MUDANÇA: Recebe o ID, acha o marca (ou falha), atualiza e salva
    public Brand updateBrandById(long id, Brand brandDetails) {
        // Reutiliza o findBrandById. Se não achar, ele já lança a exceção
        Brand brandFound = findBrandById(id);

        brandFound.setName(brandDetails.getName());

        // Verifica se o novo email já não pertence a OUTRO marca
        repository.findByName(brandDetails.getName()).ifPresent(b -> {
            if (!Objects.equals(b.getId(), brandFound.getId())) {
                throw new AlreadyExistsException("Marca _" + brandDetails.getName() + "_ já possuí esse nome!");
            }
        });

        return repository.save(brandFound);
    }

    // MUDANÇA: Apenas deleta. Se não achar, o findBrandById já lança a exceção
    public void deleteBrand(long id) {
        // Se não encontrar, o findBrandById vai lançar ResourceNotFoundException
        Brand brandToDelete = findBrandById(id);
        repository.delete(brandToDelete);
    }

}