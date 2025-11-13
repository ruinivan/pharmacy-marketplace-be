@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"domain", "domain::jpa", "pharmacy", "pharmacy::jpa", "delivery", "delivery::jpa", "exceptions", "auth", "auth::dtos"}
)
package pharmacymarketplace.user;