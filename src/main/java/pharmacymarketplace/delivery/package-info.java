@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"domain", "domain::jpa", "user", "user::jpa", "order", "order::jpa"} // Entrega depende de usuário e pedido
)
package pharmacymarketplace.delivery;