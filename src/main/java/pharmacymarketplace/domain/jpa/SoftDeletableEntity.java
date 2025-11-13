package pharmacymarketplace.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

//...domain/jpa/base/SoftDeletableEntity.java
@MappedSuperclass
@Getter
@Setter
public abstract class SoftDeletableEntity extends AuditableEntity {
    @Column(name = "deleted_at")
    private Instant deletedAt;
}