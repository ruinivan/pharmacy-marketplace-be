package pharmacymarketplace.pharmacy.dtos;

import java.util.List;

public record PharmacyDto(
        Long id,
        String legalName,
        String tradeName,
        String cnpj,
        String phone,
        String email,
        String website,
        List<PharmacyAddressDto> addresses
) {}

