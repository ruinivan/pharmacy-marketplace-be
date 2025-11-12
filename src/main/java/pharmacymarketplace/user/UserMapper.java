// Em: pharmacymarketplace/user/UserMapper.java
package pharmacymarketplace.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pharmacymarketplace.auth.dto.CreateUserRequest;
import pharmacymarketplace.user.domains.jpa.Role;
import pharmacymarketplace.user.domains.jpa.User;
import pharmacymarketplace.user.dtos.UserDto;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring") // Gera um Bean Spring [61]
public interface UserMapper {

    // Mapeamento customizado para converter Set<Role> em Set<String>
    @Mappings({
            @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))"),
            @Mapping(target = "fullName", source = "customer.fullName") // Exemplo de nested mapping
    })
    UserDto toDto(User user); // [61, 65]

    // Ignora campos que não devem ser mapeados (ex: senha)
    @Mapping(target = "hashedPassword", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    //... outros campos de BaseEntity
    User toEntity(CreateUserRequest request);

    // Método helper default usado na 'expression' acima
    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return java.util.Collections.emptySet();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}