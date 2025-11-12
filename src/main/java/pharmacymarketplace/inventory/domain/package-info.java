@org.springframework.modulith.model.ApplicationModule(
        allowedDependencies = {"domain", "pharmacy", "product"} // Inventário depende de farmácia e produto
)
package pharmacymarketplace.inventory.domain;