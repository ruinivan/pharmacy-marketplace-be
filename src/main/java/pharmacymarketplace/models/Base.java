package pharmacymarketplace.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@MappedSuperclass
public class Base {

    @Id // Marca como a Chave Primária (ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Diz ao banco para gerar o ID automaticamente
    private Long id;

    // Datas modernas
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
