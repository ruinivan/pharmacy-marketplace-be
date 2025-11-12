@org.springframework.modulith.model.ApplicationModule(
        allowedDependencies = {"domain", "user", "pharmacy", "product"} // Pedido depende de todos
)
package pharmacymarketplace.order.domain;