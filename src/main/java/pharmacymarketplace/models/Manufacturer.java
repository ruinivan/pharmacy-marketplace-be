package pharmacymarketplace.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "manufacturers")
@Getter
@Setter
@ToString
public class Manufacturer extends Pharmacy {
    @Column(nullable = false, unique = true)
    private String name;
}
