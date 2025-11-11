package pharmacymarketplace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "manufacturers")
public class Manufacturers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
