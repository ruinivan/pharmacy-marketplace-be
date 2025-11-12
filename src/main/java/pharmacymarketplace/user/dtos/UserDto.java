package pharmacymarketplace.user.dtos;

import java.util.Set;
import java.util.UUID;

public record UserDto(
        UUID publicId,
        String email,
        String fullName,
        String phoneNumber,
        Set<String> roles
) {}