@org.springframework.modulith.model.ApplicationModule(
        allowedDependencies = {"domain", "user"} // Farmácia depende do 'user' (para PharmacyStaff) e 'domain'
)
package pharmacymarketplace.pharmacy.domain;