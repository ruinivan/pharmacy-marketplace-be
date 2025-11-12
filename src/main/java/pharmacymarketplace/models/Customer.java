package pharmacymarketplace.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pharmacymarketplace.enums.CustomerTypeEnum;

import java.util.Date;

@Entity
@Table(name = "customers")
@Getter
@Setter
@ToString
public class Customer {

    @Id
    @ManyToOne(fetch = FetchType.LAZY) // LAZY é melhor para performance
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerTypeEnum customerType;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String cnpj;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;
}
