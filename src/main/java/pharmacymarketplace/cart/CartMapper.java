package pharmacymarketplace.cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pharmacymarketplace.cart.domain.jpa.CustomerCartItem;
import pharmacymarketplace.cart.dtos.CartDto;
import pharmacymarketplace.cart.dtos.CartItemDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "items", expression = "java(mapItems(items))")
    CartDto toDto(Long customerId, List<CustomerCartItem> items);

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productVariantSku", source = "productVariant.sku")
    @Mapping(target = "productName", source = "productVariant.product.name")
    @Mapping(target = "dosage", source = "productVariant.dosage")
    @Mapping(target = "packageSize", source = "productVariant.packageSize")
    CartItemDto toItemDto(CustomerCartItem item);

    default List<CartItemDto> mapItems(List<CustomerCartItem> items) {
        if (items == null) return null;
        return items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }
}

