package pharmacymarketplace.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InventoryId implements Serializable {

    private Long pharmacy;

    private Long productVariant;
}
