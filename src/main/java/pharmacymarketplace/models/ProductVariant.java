package pharmacymarketplace.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@ToString
public class ProductVariant extends Base {
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false) // Define o nome da coluna de Chave Estrangeira no BD
    private Product product;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private String packageSize;

    @Column(nullable = false)
    private String gtin;
}