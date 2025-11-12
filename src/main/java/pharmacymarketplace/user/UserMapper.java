package pharmacymarketplace.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pharmacymarketplace.auth.dtos.CreateUserRequest;
import pharmacymarketplace.user.domain.jpa.Role;
import pharmacymarketplace.user.domain.jpa.User;
import pharmacymarketplace.user.dtos.UserDto;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring") // [20, 21]
public interface UserMapper {

    @Mappings({
            @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))"),
            @Mapping(target = "fullName", source = "customer.fullName")
    })
    UserDto toDto(User user); // [22]

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "hashedPassword", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "pharmacyStaff", ignore = true)
    @Mapping(target = "deliveryPersonnel", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    User toEntity(CreateUserRequest request);

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return java.util.Collections.emptySet();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}