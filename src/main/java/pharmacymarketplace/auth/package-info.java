@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"domain", "user", "user::jpa", "user::enums", "exceptions"}
)
package pharmacymarketplace.auth;