//...audit/repository/mongo/AuditLogMongoRepository.java
package pharmacymarketplace.audit.repositories.mongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import pharmacymarketplace.audit.domains.mongo.AuditLogDocument;

public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {
}