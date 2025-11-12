@org.springframework.modulith.model.ApplicationModule(
        allowedDependencies = {"domain", "user", "order"} // Entrega depende de usuário e pedido
)
package pharmacymarketplace.delivery.domain;