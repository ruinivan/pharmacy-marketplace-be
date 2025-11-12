package pharmacymarketplace.product.domains.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// pharmacymarketplace/product/domain/jpa/ProductCategoryId.java
@Embeddable // [75, 76, 77]
@Getter
@Setter
@EqualsAndHashCode
public class ProductCategoryId implements Serializable {
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "category_id")
    private Long categoryId;
}
