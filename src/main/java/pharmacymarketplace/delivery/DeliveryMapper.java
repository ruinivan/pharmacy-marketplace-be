package pharmacymarketplace.delivery;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pharmacymarketplace.delivery.domain.jpa.Delivery;
import pharmacymarketplace.delivery.dtos.DeliveryDto;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "orderCode", source = "order.orderCode")
    @Mapping(target = "deliveryPersonnelId", source = "deliveryPersonnel.id")
    @Mapping(target = "deliveryPersonnelName", source = "deliveryPersonnel.user.email")
    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "addressStreet", source = "address.street")
    DeliveryDto toDto(Delivery delivery);
}

