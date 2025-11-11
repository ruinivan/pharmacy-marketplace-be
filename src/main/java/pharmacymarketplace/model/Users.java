package pharmacymarketplace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // Diz ao Spring que esta classe é uma tabela no banco de dados
@Table(name = "users")
@Getter
@Setter
@ToString
public class Users {

    @Id // Marca como a Chave Primária (ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Diz ao banco para gerar o ID automaticamente
    private Long id;

    private String nome;
    private String email;
    private String senha; // Apenas para exemplo, em um app real, isso deve ser criptografado!
}