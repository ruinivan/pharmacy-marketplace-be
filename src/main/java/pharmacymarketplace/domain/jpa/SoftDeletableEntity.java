package pharmacymarketplace.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.FilterDef;

import java.time.Instant;

@MappedSuperclass
@FilterDef(name = "deletedFilter", defaultCondition = "deleted_at IS NULL")
@Getter
@Setter
public abstract class SoftDeletableEntity extends AuditableEntity {
    @Column(name = "deleted_at")
    private Instant deletedAt;
}