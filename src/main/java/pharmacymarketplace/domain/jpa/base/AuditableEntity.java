package pharmacymarketplace.domain.jpa.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

//...domain/jpa/base/AuditableEntity.java
@MappedSuperclass
@Getter
@Setter
public abstract class AuditableEntity extends BaseEntity {
    @CreationTimestamp // Gerenciado pelo Hibernate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp // Gerenciado pelo Hibernate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
