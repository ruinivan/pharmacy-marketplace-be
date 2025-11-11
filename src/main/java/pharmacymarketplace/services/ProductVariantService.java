// Em: /services/ProductVariantService.java
package pharmacymarketplace.services;

import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.model.Product;
import pharmacymarketplace.model.ProductVariant;
import pharmacymarketplace.repository.ProductVariantRepository;
import pharmacymarketplace.exceptions.ResourceNotFoundException; // Importe

import java.util.ArrayList;
import java.util.Objects;

@Service
public class ProductVariantService {

    private final ProductVariantRepository repository;

    // Injeção por construtor (como falamos antes)
    public ProductVariantService(ProductVariantRepository repository) {
        this.repository = repository;
    }

    public ProductVariant createProductVariant(ProductVariant productVariant) {
        // A lógica de "já existe" continua igual, lançando a exceção
        repository.findBySku(productVariant.getSku()).ifPresent(pv -> {
            throw new AlreadyExistsException("Produto com SKU _" + productVariant.getSku() + "_ já existe!");
        });

        return repository.save(productVariant);
    }

    public ArrayList<ProductVariant> listAllProductVariants() {
        return (ArrayList<ProductVariant>) repository.findAll();
    }

    // MUDANÇA: Não retorna mais Optional<ProductVariant>
    public ProductVariant findProductVariantById(long id) {
        // Encontra ou lança a exceção 404
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID: " + id + " não encontrado!"));
    }

    // MUDANÇA: Recebe o ID, acha o produto (ou falha), atualiza e salva
    public ProductVariant updateProductVariantById(long id, ProductVariant productDetails) {
        // Reutiliza o findProductVariantById. Se não achar, ele já lança a exceção
        ProductVariant productFound = findProductVariantById(id);


        // Verifica se o novo email já não pertence a OUTRO produto
        repository.findBySku(productDetails.getSku()).ifPresent(pv -> {
            if (!Objects.equals(pv.getId(), productFound.getId())) {
                throw new AlreadyExistsException("SKU _" + productDetails.getSku() + "_ já está em uso por outro produto!");
            }
        });

        productFound.setSku(productDetails.getSku());
        productFound.setDosage(productDetails.getDosage());
        productFound.setPackageSize(productDetails.getPackageSize());
        productFound.setGtin(productDetails.getGtin());

        return repository.save(productFound);
    }

    // MUDANÇA: Apenas deleta. Se não achar, o findProductVariantById já lança a exceção
    public void deleteProductVariantById(long id) {
        // Se não encontrar, o findProductVariantById vai lançar ResourceNotFoundException
        ProductVariant productToDelete = findProductVariantById(id);
        repository.delete(productToDelete);
    }

}