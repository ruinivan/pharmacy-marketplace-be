@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"domain", "domain::jpa", "user", "user::jpa", "pharmacy", "pharmacy::jpa", "product", "product::jpa"} // Pedido depende de todos
)
package pharmacymarketplace.order;