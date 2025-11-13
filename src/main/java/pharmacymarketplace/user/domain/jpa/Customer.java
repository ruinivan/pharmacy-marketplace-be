// pharmacymarketplace/user/domain/jpa/Customer.java
package pharmacymarketplace.user.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.domain.jpa.BaseEntity;
import pharmacymarketplace.user.enums.CustomerTypeEnum;

import java.time.LocalDate;

@Entity
@Table(name = "customers")
@Getter @Setter
public class Customer extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Esta é a chave! Liga o ID desta entidade ao ID do 'User'
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerTypeEnum customerType; // Enum: INDIVIDUAL, LEGAL_ENTITY

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String cnpj;

    @Column(name = "birth_date")
    private LocalDate birthDate;

}
