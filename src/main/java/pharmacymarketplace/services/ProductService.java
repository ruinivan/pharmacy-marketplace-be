// Em: /services/ProductService.java
package pharmacymarketplace.services;

import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.model.Product;
import pharmacymarketplace.repository.ProductRepository;
import pharmacymarketplace.exceptions.ResourceNotFoundException; // Importe

import java.util.ArrayList;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository repository;

    // Injeção por construtor (como falamos antes)
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(Product product) {
        // A lógica de "já existe" continua igual, lançando a exceção
        repository.findProductByName(product.getName()).ifPresent(p -> {
            throw new AlreadyExistsException("Produto com nome _" + product.getName() + "_ já existe!");
        });

        return repository.save(product);
    }

    public ArrayList<Product> listAllProducts() {
        return (ArrayList<Product>) repository.findAll();
    }

    // MUDANÇA: Não retorna mais Optional<Product>
    public Product findProductById(long id) {
        // Encontra ou lança a exceção 404
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID: " + id + " não encontrado!"));
    }

    // MUDANÇA: Recebe o ID, acha o produto (ou falha), atualiza e salva
    public Product updateProductById(long id, Product productDetails) {
        // Reutiliza o findProductById. Se não achar, ele já lança a exceção
        Product productFound = findProductById(id);


        // Verifica se o novo email já não pertence a OUTRO produto
        repository.findProductByName(productDetails.getName()).ifPresent(p -> {
            if (!Objects.equals(p.getId(), productFound.getId())) {
                throw new AlreadyExistsException("Nome _" + productDetails.getName() + "_ já está em uso por outro produto!");
            }
        });

        return repository.save(productFound);
    }

    // MUDANÇA: Apenas deleta. Se não achar, o findProductById já lança a exceção
    public void deleteProductById(long id) {
        // Se não encontrar, o findProductById vai lançar ResourceNotFoundException
        Product productToDelete = findProductById(id);
        repository.delete(productToDelete);
    }

}