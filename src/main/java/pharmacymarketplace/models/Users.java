package pharmacymarketplace.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // Diz ao Spring que esta classe é uma tabela no banco de dados
@Table(name = "users")
@Getter
@Setter
@ToString
public class Users extends Base {
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha; // Apenas para exemplo, em um app real, isso deve ser criptografado!
}