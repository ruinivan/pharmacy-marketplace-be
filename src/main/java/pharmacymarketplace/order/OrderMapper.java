package pharmacymarketplace.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pharmacymarketplace.order.domain.jpa.Order;
import pharmacymarketplace.order.domain.jpa.OrderItem;
import pharmacymarketplace.order.domain.jpa.Payment;
import pharmacymarketplace.order.domain.jpa.Prescription;
import pharmacymarketplace.order.dtos.OrderDto;
import pharmacymarketplace.order.dtos.OrderItemDto;
import pharmacymarketplace.order.dtos.PaymentDto;
import pharmacymarketplace.order.dtos.PrescriptionDto;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "pharmacyId", source = "pharmacy.id")
    @Mapping(target = "items", expression = "java(mapItems(order.getItems()))")
    @Mapping(target = "payments", expression = "java(mapPayments(order.getPayments()))")
    @Mapping(target = "prescription", source = "prescription")
    OrderDto toDto(Order order);

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productVariantSku", source = "productVariant.sku")
    @Mapping(target = "productName", source = "productVariant.product.name")
    OrderItemDto toItemDto(OrderItem item);

    PrescriptionDto toPrescriptionDto(Prescription prescription);

    PaymentDto toPaymentDto(Payment payment);

    default Set<OrderItemDto> mapItems(Set<OrderItem> items) {
        if (items == null) return null;
        return items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toSet());
    }

    default Set<PaymentDto> mapPayments(Set<Payment> payments) {
        if (payments == null) return null;
        return payments.stream()
                .map(this::toPaymentDto)
                .collect(Collectors.toSet());
    }
}

