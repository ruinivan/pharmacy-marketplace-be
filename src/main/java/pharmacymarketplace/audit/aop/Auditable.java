package pharmacymarketplace.audit.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//...audit/aop/Auditable.java (A anotação customizada)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    String action(); // Ex: "UPDATE_PRICE"
    String tableName(); // Ex: "inventory"
}
