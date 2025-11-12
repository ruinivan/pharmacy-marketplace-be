//...audit/repository/mongo/AuditLogMongoRepository.java
package pharmacymarketplace.audit.repository.mongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import pharmacymarketplace.audit.domain.mongo.AuditLogDocument;

public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {
}