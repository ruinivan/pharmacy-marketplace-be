package pharmacymarketplace.pharmacy.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreatePharmacyAddressRequest(
        @NotBlank(message = "Rua é obrigatória")
        String street,

        @NotBlank(message = "Bairro é obrigatório")
        String neighborhood,

        @NotBlank(message = "Cidade é obrigatória")
        String city,

        @NotBlank(message = "Estado é obrigatório")
        String state,

        @NotBlank(message = "País é obrigatório")
        String country,

        String postalCode,

        String complement,

        String openingHours
) {}

