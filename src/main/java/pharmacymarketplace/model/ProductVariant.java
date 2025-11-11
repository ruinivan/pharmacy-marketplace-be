package pharmacymarketplace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@ToString
public class ProductVariant {

    @Id // Marca como a Chave Primária (ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Diz ao banco para gerar o ID automaticamente
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id") // Define o nome da coluna de Chave Estrangeira no BD
    private Product product;

    private String sku;
    private String dosage;
    private String packageSize;
    private String gtin;
    private LocalDateTime deletedAt;
}