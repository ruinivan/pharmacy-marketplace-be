package pharmacymarketplace.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacymarketplace.model.Product;
import pharmacymarketplace.model.Users;
import pharmacymarketplace.repository.ProductRepository;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        Optional<Product> productAlreadyExists = productRepository.findById(product.getId());
        if (productAlreadyExists.isPresent()) {
            throw new Error("Produto já existe!");
        }
        return productRepository.save(product);
    }


}
