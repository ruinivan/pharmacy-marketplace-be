package pharmacymarketplace.audit.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pharmacymarketplace.audit.domain.mongo.AuditLogDocument;
import pharmacymarketplace.audit.repository.mongo.AuditLogMongoRepository;

//...audit/aop/AuditAspect.java
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogMongoRepository auditLogRepository;
    private final ObjectMapper objectMapper; // Para serializar 'before' e 'after'

    @AfterReturning(
            pointcut = "@annotation(auditable)",
            returning = "result"
    ) // [91, 92, 93]
    public void logAudit(JoinPoint joinPoint, Auditable auditable, Object result) {

        Long userId = getCurrentUserId(); // Função helper para buscar do SecurityContext

        // Extrair dados (ex: 'before' e 'after' dos args ou do 'result')
        // Esta parte é complexa e depende da assinatura do método
        String oldValue = "..."; // [94]
        String newValue = "...";
        Long primaryKey =...; // Extrair a PK do objeto de resultado ou args

        AuditLogDocument logEntry = new AuditLogDocument(
                null,
                auditable.tableName(),
                primaryKey,
                auditable.action(), // ou um nome de coluna específico
                oldValue,
                newValue,
                userId,
                null // createdAt é automático
        );

        auditLogRepository.save(logEntry); // [95]
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||!authentication.isAuthenticated()) {
            return null; // Ação do sistema
        }
        //... lógica para buscar o ID do 'User' (MySQL) a partir do 'UserDetails'
        return 1L; // Placeholder
    }
}
