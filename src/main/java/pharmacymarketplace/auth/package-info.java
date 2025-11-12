@org.springframework.modulith.model.ApplicationModule(
        allowedDependencies = {"domain", "user", "exceptions"} // Auth depende de 'user' e 'exceptions'
)
package pharmacymarketplace.auth;