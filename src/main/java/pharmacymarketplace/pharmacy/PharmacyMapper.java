package pharmacymarketplace.pharmacy;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.pharmacy.domain.jpa.PharmacyAddress;
import pharmacymarketplace.pharmacy.dtos.PharmacyDto;
import pharmacymarketplace.pharmacy.dtos.PharmacyAddressDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PharmacyMapper {

    @Mapping(target = "addresses", expression = "java(mapAddresses(pharmacy.getAddresses()))")
    PharmacyDto toDto(Pharmacy pharmacy);

    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "neighborhood", source = "address.neighborhood")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "state", source = "address.state")
    @Mapping(target = "postalCode", source = "address.postalCode")
    PharmacyAddressDto toAddressDto(PharmacyAddress pharmacyAddress);

    default List<PharmacyAddressDto> mapAddresses(java.util.Set<PharmacyAddress> addresses) {
        if (addresses == null) return null;
        return addresses.stream()
                .map(this::toAddressDto)
                .collect(Collectors.toList());
    }
}

