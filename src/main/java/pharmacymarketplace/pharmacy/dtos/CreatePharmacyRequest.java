package pharmacymarketplace.pharmacy.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePharmacyRequest(
        @NotBlank(message = "Razão social é obrigatória")
        String legalName,

        @NotBlank(message = "Nome fantasia é obrigatório")
        String tradeName,

        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,

        @NotBlank(message = "Telefone é obrigatório")
        String phone,

        String email,

        String website,

        @NotNull(message = "Endereço é obrigatório")
        CreatePharmacyAddressRequest address
) {}

