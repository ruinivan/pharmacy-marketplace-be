package pharmacymarketplace.favorite.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.product.domain.jpa.ProductVariant;
import pharmacymarketplace.user.domain.jpa.Customer;

@Entity
@Table(name = "favorites", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"customer_id", "product_variant_id"}),
    @UniqueConstraint(columnNames = {"customer_id", "pharmacy_id"})
})
@Getter
@Setter
public class Favorite extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    @Enumerated(EnumType.STRING)
    @Column(name = "favorite_type", nullable = false)
    private FavoriteType favoriteType;

    public enum FavoriteType {
        PRODUCT,
        PHARMACY
    }
}

