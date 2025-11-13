package pharmacymarketplace.pharmacy.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable // [75, 76, 77]
@Getter
@Setter
@EqualsAndHashCode
public class PharmacyStaffId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;
}
