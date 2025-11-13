@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"domain", "domain::jpa", "order", "order::jpa"}
)
package pharmacymarketplace.product;