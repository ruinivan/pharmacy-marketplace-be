package pharmacymarketplace.user.domain.jpa;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// pharmacymarketplace/user/domain/jpa/CustomerAddressId.java
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class CustomerAddressId implements Serializable {

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "address_id")
    private Long addressId;
}