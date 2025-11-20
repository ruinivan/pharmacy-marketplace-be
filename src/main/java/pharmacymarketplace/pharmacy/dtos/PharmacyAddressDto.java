package pharmacymarketplace.pharmacy.dtos;

public record PharmacyAddressDto(
        Long addressId,
        String street,
        String neighborhood,
        String city,
        String state,
        String postalCode,
        Boolean isPrimary,
        String openingHours
) {}

