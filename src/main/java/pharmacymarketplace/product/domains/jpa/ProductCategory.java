package pharmacymarketplace.product.domains.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// pharmacymarketplace/product/domain/jpa/ProductCategory.java
@Entity
@Table(name = "product_categories")
@Getter
@Setter
public class ProductCategory {

    @EmbeddedId
    private ProductCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId") // Mapeia a parte 'productId' do @EmbeddedId
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId") // Mapeia a parte 'categoryId' do @EmbeddedId
    @JoinColumn(name = "category_id")
    private Category category;
}