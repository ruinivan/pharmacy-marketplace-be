package pharmacymarketplace.user.domain.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pharmacymarketplace.user.enums.AddressTypeEnum;

// pharmacymarketplace/user/domain/jpa/CustomerAddress.java
@Entity
@Table(name = "customer_addresses")
@Getter
@Setter
public class CustomerAddress {

    @EmbeddedId
    private CustomerAddressId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Cascata para salvar novos endereços
    @MapsId("addressId")
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    private AddressTypeEnum addressType;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;
}

