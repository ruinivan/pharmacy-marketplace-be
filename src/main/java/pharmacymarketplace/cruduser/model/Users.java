package pharmacymarketplace.cruduser.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity // Diz ao Spring que esta classe é uma tabela no banco de dados
@Data   // Mágica do Lombok: cria getters, setters, toString, etc. automaticamente
public class Users {

    @Id // Marca como a Chave Primária (ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Diz ao banco para gerar o ID automaticamente
    private Long id;

    private String nome;
    private String email;
    private String senha; // Apenas para exemplo, em um app real, isso deve ser criptografado!
}