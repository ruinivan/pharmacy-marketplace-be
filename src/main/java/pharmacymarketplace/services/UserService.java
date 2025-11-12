// Em: /services/UserService.java
package pharmacymarketplace.services;

import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.models.Users;
import pharmacymarketplace.repositories.UserRepository;
import pharmacymarketplace.exceptions.ResourceNotFoundException; // Importe

import java.util.ArrayList;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository repository;

    // Injeção por construtor (como falamos antes)
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Users createUser(Users user) {
        // A lógica de "já existe" continua igual, lançando a exceção
        repository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new AlreadyExistsException("Usuário com email _" + user.getEmail() + "_ já existe!");
        });

        return repository.save(user);
    }

    public ArrayList<Users> listAllUsers() {
        return (ArrayList<Users>) repository.findAll();
    }

    // MUDANÇA: Não retorna mais Optional<Users>
    public Users findUserById(long id) {
        // Encontra ou lança a exceção 404
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID: " + id + " não encontrado!"));
    }

    // MUDANÇA: Recebe o ID, acha o usuário (ou falha), atualiza e salva
    public Users updateUserById(long id, Users userDetails) {
        // Reutiliza o findUserById. Se não achar, ele já lança a exceção
        Users userFound = findUserById(id);

        userFound.setNome(userDetails.getNome());
        userFound.setEmail(userDetails.getEmail());
        userFound.setSenha(userDetails.getSenha());

        // Verifica se o novo email já não pertence a OUTRO usuário
        repository.findByEmail(userDetails.getEmail()).ifPresent(u -> {
            if (!Objects.equals(u.getId(), userFound.getId())) {
                throw new AlreadyExistsException("Email _" + userDetails.getEmail() + "_ já está em uso por outro usuário!");
            }
        });

        return repository.save(userFound);
    }

    // MUDANÇA: Apenas deleta. Se não achar, o findUserById já lança a exceção
    public void deleteUserById(long id) {
        // Se não encontrar, o findUserById vai lançar ResourceNotFoundException
        Users userToDelete = findUserById(id);
        repository.delete(userToDelete);
    }

}