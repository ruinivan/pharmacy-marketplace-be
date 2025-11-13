@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"domain", "pharmacy", "pharmacy::jpa", "product", "product::jpa"} // Inventário depende de farmácia e produto
)
package pharmacymarketplace.inventory;