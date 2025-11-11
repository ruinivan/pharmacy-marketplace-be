package pharmacymarketplace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "brand")
@Getter
@Setter
@ToString
public class Brand {
    @Id // Marca como a Chave Primária (ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Diz ao banco para gerar o ID automaticamente
    private Long id;

    private String name;
}
