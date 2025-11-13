@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"domain", "domain::jpa", "user", "user::jpa"} // Farmácia depende do 'user' (para PharmacyStaff) e 'domain'
)
package pharmacymarketplace.pharmacy;