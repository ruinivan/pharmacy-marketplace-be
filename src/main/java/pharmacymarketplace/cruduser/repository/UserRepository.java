package pharmacymarketplace.cruduser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacymarketplace.cruduser.model.Users;

// Nós estendemos JpaRepository<TipoDaEntidade, TipoDoId>
public interface UserRepository extends JpaRepository<Users, Long> {

    // É SÓ ISSO!
    // O JpaRepository já nos dá:
    // - save() (para Criar e Atualizar)
    // - findAll() (para Ler todos)
    // - findById() (para Ler um)
    // - deleteById() (para Deletar)
    // E muito mais!
}