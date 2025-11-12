package pharmacymarketplace.audit.domains.mongo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "audit_log")
public record AuditLogDocument(
        @Id
        String id,

        String tableName,

        Long rowPk, // PK da entidade (MySQL)

        String columnName,

        String oldValue, // Pode armazenar JSON serializado

        String newValue, // Pode armazenar JSON serializado

        Long changedByUserId, // FK para 'users' (MySQL)

        @CreatedDate
        Instant createdAt
) {}