package pharmacymarketplace.audit.aop;

// import com.fasterxml.jackson.databind.ObjectMapper; // Descomente quando for usar
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pharmacymarketplace.audit.domain.mongo.AuditLogDocument;
import pharmacymarketplace.audit.repository.mongo.AuditLogMongoRepository;
import pharmacymarketplace.domain.jpa.BaseEntity;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogMongoRepository auditLogRepository;
    // private final ObjectMapper objectMapper; // Para serializar 'before' e 'after' [8]

    @AfterReturning(
            pointcut = "@annotation(auditable)",
            returning = "result"
    ) // [9, 10]
    public void logAudit(JoinPoint joinPoint, Auditable auditable, Object result) {

        Long userId = getCurrentUserId(); // Função helper para buscar do SecurityContext

        // Lógica de placeholder (mas com sintaxe correta)
        String oldValue = "placeholder_old_value";
        String newValue = "placeholder_new_value";
        Long primaryKey = null; // [8]

        // Tenta extrair a PK do objeto retornado, se ele for uma BaseEntity
        if (result instanceof BaseEntity) {
            primaryKey = ((BaseEntity) result).getId();
        }
        // Em um caso real, você usaria 'joinPoint.getArgs()' para pegar o "antes"
        // e 'result' para o "depois" [10, 11]

        AuditLogDocument logEntry = new AuditLogDocument(
                null,
                auditable.tableName(),
                primaryKey,
                auditable.action(), // ou um nome de coluna específico
                oldValue,
                newValue,
                userId,
                null // createdAt é automático [12]
        );

        auditLogRepository.save(logEntry); // [13]
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||!authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null; // Ação do sistema
        }

        // Esta é uma implementação placeholder.
        // Em um app real, você injetaria um UserService para buscar
        // o ID do usuário (do MySQL) com base no email (authentication.getName())
        // Ex: return userService.findIdByEmail(authentication.getName());
        return 1L; // Placeholder
    }
}