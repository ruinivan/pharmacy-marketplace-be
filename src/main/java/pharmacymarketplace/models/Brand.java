package pharmacymarketplace.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "brand")
@Getter
@Setter
@ToString
public class Brand extends Base {

    @Column(nullable = false, unique = true)
    private String name;
}
