@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"user::jpa", "order::jpa", "delivery::jpa"}
)
package pharmacymarketplace.notification;

