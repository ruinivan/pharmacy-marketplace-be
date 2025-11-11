package pharmacymarketplace.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "products") // É bom definir o nome da tabela
@Getter
@Setter
@ToString
public class Product {

    @Id // Marca como a Chave Primária (ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Diz ao banco para gerar o ID automaticamente
    private Long id;

    private String public_id;
    private String name;
    private String description;
    private String anvisa_code;
    private String active_principle;
    private String pharmaceutical_form;
    private boolean is_prescription_required;
    private String controlled_substance_list;


    // Relacionamentos corretos
    @ManyToOne(fetch = FetchType.LAZY) // LAZY é melhor para performance
    @JoinColumn(name = "brand_id")
    private Brand brand; // O objeto, não o ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer; // O objeto, não o ID

    // Datas modernas
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

}
