@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"user::jpa", "product::jpa"}
)
package pharmacymarketplace.cart;