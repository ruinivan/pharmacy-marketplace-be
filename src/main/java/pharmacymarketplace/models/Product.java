package pharmacymarketplace.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "products") // É bom definir o nome da tabela
@Getter
@Setter
@ToString
public class Product extends Base {

    @Column(name = "public_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID publicId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "anvisa_code", nullable = false, unique = true)
    private String anvisaCode;

    @Column(name = "active_principle", nullable = false)
    private String activePrinciple;

    @Column(name = "pharmaceutical_form", nullable = false)
    private String pharmaceuticalForm;

    @Column(name = "is_prescription_required", nullable = false)
    private boolean isPrescriptionRequired;

    @Column(name = "controlled_substance_list", nullable = false)
    private String controlledSubstanceList;


    // Relacionamentos corretos
    @ManyToOne(fetch = FetchType.LAZY) // LAZY é melhor para performance
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand; // O objeto, não o ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer; // O objeto, não o ID
}
