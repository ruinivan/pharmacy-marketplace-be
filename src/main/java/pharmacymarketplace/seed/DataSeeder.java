package pharmacymarketplace.seed;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pharmacymarketplace.delivery.domain.jpa.DeliveryPersonnel;
import pharmacymarketplace.delivery.repository.jpa.DeliveryPersonnelRepository;
import pharmacymarketplace.inventory.domain.jpa.Inventory;
import pharmacymarketplace.inventory.domain.jpa.InventoryId;
import pharmacymarketplace.inventory.repository.jpa.InventoryRepository;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.pharmacy.domain.jpa.PharmacyAddress;
import pharmacymarketplace.pharmacy.repository.jpa.PharmacyRepository;
import pharmacymarketplace.product.domain.jpa.*;
import pharmacymarketplace.product.domain.jpa.ProductCategoryId;
import pharmacymarketplace.product.repository.jpa.*;
import pharmacymarketplace.user.domain.jpa.*;
import pharmacymarketplace.user.enums.CustomerTypeEnum;
import pharmacymarketplace.user.repository.jpa.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = false)
public class DataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final PharmacyRepository pharmacyRepository;
    private final DeliveryPersonnelRepository deliveryPersonnelRepository;
    private final BrandRepository brandRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final InventoryRepository inventoryRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void seed() {
        logger.info("=== INICIANDO SEED DE DADOS ===");
        try {
            // Busca ou cria as roles
        Role roleCustomer = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> createRole("ROLE_CUSTOMER"));
        Role rolePharmacyAdmin = roleRepository.findByName("ROLE_PHARMACY_ADMIN")
                .orElseGet(() -> createRole("ROLE_PHARMACY_ADMIN"));
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> createRole("ROLE_ADMIN"));
        Role roleDelivery = roleRepository.findByName("ROLE_DELIVERY_PERSONNEL")
                .orElseGet(() -> createRole("ROLE_DELIVERY_PERSONNEL"));

        // Cria usuários apenas se não existirem
        if (userRepository.findByEmail("admin@pharmacy.com").isEmpty()) {
            createUser("admin@pharmacy.com", "admin123", "Admin Sistema", Set.of(roleAdmin));
        }
        
        if (userRepository.findByEmail("cliente@test.com").isEmpty()) {
            User customerUser1 = createUser("cliente@test.com", "cliente123", "João Silva", Set.of(roleCustomer));
            Long userId1 = customerUser1.getId();
            if (userId1 != null && customerRepository.findById(userId1).isEmpty()) {
                createCustomer(customerUser1, "João Silva", CustomerTypeEnum.INDIVIDUAL, "12345678900");
            }
        }
        
        if (userRepository.findByEmail("cliente2@test.com").isEmpty()) {
            User customerUser2 = createUser("cliente2@test.com", "cliente123", "Maria Santos", Set.of(roleCustomer));
            Long userId2 = customerUser2.getId();
            if (userId2 != null && customerRepository.findById(userId2).isEmpty()) {
                createCustomer(customerUser2, "Maria Santos", CustomerTypeEnum.INDIVIDUAL, "98765432100");
            }
        }
        
        final User pharmacyUser1 = userRepository.findByEmail("farmacia@test.com")
                .orElseGet(() -> createUser("farmacia@test.com", "farmacia123", "Maria Farmacêutica", Set.of(rolePharmacyAdmin)));
        
        final User pharmacyUser2 = userRepository.findByEmail("farmacia2@test.com")
                .orElseGet(() -> createUser("farmacia2@test.com", "farmacia123", "Pedro Farmacêutico", Set.of(rolePharmacyAdmin)));
        
        final User pharmacyUser3 = userRepository.findByEmail("farmacia3@test.com")
                .orElseGet(() -> createUser("farmacia3@test.com", "farmacia123", "Ana Farmacêutica", Set.of(rolePharmacyAdmin)));
        
        final User deliveryUser1 = userRepository.findByEmail("entregador@test.com")
                .orElseGet(() -> createUser("entregador@test.com", "entregador123", "Carlos Entregador", Set.of(roleDelivery)));
        
        final User deliveryUser2 = userRepository.findByEmail("entregador2@test.com")
                .orElseGet(() -> createUser("entregador2@test.com", "entregador123", "Ana Entregadora", Set.of(roleDelivery)));

        // Cria endereços e outras entidades apenas se necessário
        if (addressRepository.count() == 0) {
            createAddress("SQN 105 Bloco A, 123", "Brasília", "DF", "70736-020");
            createAddress("SQN 106 Bloco B, 456", "Brasília", "DF", "70736-030");
        }

        Pharmacy pharmacy1;
        Pharmacy pharmacy2;
        Pharmacy pharmacy3;
        
        if (pharmacyRepository.count() == 0) {
            pharmacy1 = createPharmacy("Farmácia Central LTDA", "Farmácia Central", "12345678000190", "61987654321", "farmacia@central.com", pharmacyUser1);
            Address pharmacyAddressEntity1 = createAddressForPharmacy("SCS Quadra 1 Bloco A, Loja 10", "Brasília", "DF", "70302-000");
            createPharmacyAddress(pharmacy1, pharmacyAddressEntity1);

            pharmacy2 = createPharmacy("Farmácia Popular LTDA", "Farmácia Popular", "98765432000180", "61976543210", "farmacia2@popular.com", pharmacyUser2);
            Address pharmacyAddressEntity2 = createAddressForPharmacy("SQN 305 Bloco A, Loja 25", "Brasília", "DF", "70736-050");
            createPharmacyAddress(pharmacy2, pharmacyAddressEntity2);
            
            pharmacy3 = createPharmacy("Farmácia Saúde Total LTDA", "Farmácia Saúde Total", "11122233000144", "61965432100", "farmacia3@saude.com", pharmacyUser3);
            Address pharmacyAddressEntity3 = createAddressForPharmacy("SQN 405 Bloco B, Loja 15", "Brasília", "DF", "70740-100");
            createPharmacyAddress(pharmacy3, pharmacyAddressEntity3);
        } else {
            // Busca as farmácias existentes
            pharmacy1 = pharmacyRepository.findAll().stream()
                    .filter(p -> "12345678000190".equals(p.getCnpj()))
                    .findFirst()
                    .orElseGet(() -> createPharmacy("Farmácia Central LTDA", "Farmácia Central", "12345678000190", "61987654321", "farmacia@central.com", pharmacyUser1));
            pharmacy2 = pharmacyRepository.findAll().stream()
                    .filter(p -> "98765432000180".equals(p.getCnpj()))
                    .findFirst()
                    .orElseGet(() -> createPharmacy("Farmácia Popular LTDA", "Farmácia Popular", "98765432000180", "61976543210", "farmacia2@popular.com", pharmacyUser2));
            pharmacy3 = pharmacyRepository.findAll().stream()
                    .filter(p -> "11122233000144".equals(p.getCnpj()))
                    .findFirst()
                    .orElseGet(() -> createPharmacy("Farmácia Saúde Total LTDA", "Farmácia Saúde Total", "11122233000144", "61965432100", "farmacia3@saude.com", pharmacyUser3));
        }

        // Cria entregadores apenas se não existirem
        // Garante que o User está salvo antes de criar DeliveryPersonnel
        if (deliveryUser1 != null) {
            Long userId1 = deliveryUser1.getId();
            if (userId1 != null && deliveryPersonnelRepository.findById(userId1).isEmpty()) {
                try {
                    createDeliveryPersonnel(deliveryUser1, "CNH123456", "Moto Honda CG 160");
                } catch (Exception e) {
                    logger.warn("Erro ao criar entregador 1: {}", e.getMessage());
                }
            }
        }
        if (deliveryUser2 != null) {
            Long userId2 = deliveryUser2.getId();
            if (userId2 != null && deliveryPersonnelRepository.findById(userId2).isEmpty()) {
                try {
                    createDeliveryPersonnel(deliveryUser2, "CNH789012", "Carro Fiat Uno");
                } catch (Exception e) {
                    logger.warn("Erro ao criar entregador 2: {}", e.getMessage());
                }
            }
        }

        // Cria produtos e inventário sempre (verificando se já existem)
        seedProductsAndInventory(pharmacy1, pharmacy2, pharmacy3);
        } catch (Exception e) {
            // Log do erro mas não impede a inicialização do backend
            logger.error("=== ERRO AO EXECUTAR SEED ===", e);
            logger.error("Erro: {}", e.getMessage());
        }
        logger.info("=== SEED DE DADOS CONCLUÍDO ===");
    }

    @Transactional
    private void seedProductsAndInventory(Pharmacy pharmacy1, Pharmacy pharmacy2, Pharmacy pharmacy3) {
        logger.info("=== INICIANDO SEED DE PRODUTOS E INVENTÁRIO ===");
        // Cria marcas e fabricantes (verificando se já existem)
        Brand brand1 = brandRepository.findByName("MedLab")
                .orElseGet(() -> createBrand("MedLab"));
        Brand brand2 = brandRepository.findByName("PharmaCorp")
                .orElseGet(() -> createBrand("PharmaCorp"));
        Brand brand3 = brandRepository.findByName("BioHealth")
                .orElseGet(() -> createBrand("BioHealth"));
        Manufacturer manufacturer1 = manufacturerRepository.findByName("Farmacêutica XYZ")
                .orElseGet(() -> createManufacturer("Farmacêutica XYZ"));
        Manufacturer manufacturer2 = manufacturerRepository.findByName("Indústria Farmacêutica ABC")
                .orElseGet(() -> createManufacturer("Indústria Farmacêutica ABC"));
        Manufacturer manufacturer3 = manufacturerRepository.findByName("Laboratórios Vida")
                .orElseGet(() -> createManufacturer("Laboratórios Vida"));
        
        // Cria categorias (verificando se já existem)
        Category categoryMed = categoryRepository.findByName("Medicamentos")
                .orElseGet(() -> createCategory("Medicamentos", "Medicamentos gerais"));
        Category categoryHig = categoryRepository.findByName("Higiene")
                .orElseGet(() -> createCategory("Higiene", "Produtos de higiene pessoal"));
        Category categoryVit = categoryRepository.findByName("Vitaminas")
                .orElseGet(() -> createCategory("Vitaminas", "Suplementos vitamínicos"));
        Category categoryCos = categoryRepository.findByName("Cosméticos")
                .orElseGet(() -> createCategory("Cosméticos", "Produtos cosméticos"));
        Category categoryInf = categoryRepository.findByName("Infantil")
                .orElseGet(() -> createCategory("Infantil", "Produtos para crianças"));
        
        // Cria ou busca produtos (verificando se já existem pelo nome)
        Product p1 = findOrCreateProduct("Paracetamol 500mg", "Analgésico e antitérmico", "Paracetamol", "Nenhuma", brand1, manufacturer1, false);
        ProductVariant v1 = findOrCreateVariant(p1, "PAR500-30", "Caixa com 30 comprimidos", BigDecimal.valueOf(15.90));
        ProductVariant v2 = findOrCreateVariant(p1, "PAR500-60", "Caixa com 60 comprimidos", BigDecimal.valueOf(28.90));
        ensureProductCategory(p1, categoryMed);
        
        Product p2 = findOrCreateProduct("Ibuprofeno 400mg", "Anti-inflamatório e analgésico", "Ibuprofeno", "Nenhuma", brand1, manufacturer1, false);
        ProductVariant v3 = findOrCreateVariant(p2, "IBU400-20", "Caixa com 20 comprimidos", BigDecimal.valueOf(18.50));
        ensureProductCategory(p2, categoryMed);
        
        Product p3 = findOrCreateProduct("Dipirona 500mg", "Analgésico e antitérmico", "Dipirona", "Nenhuma", brand2, manufacturer2, false);
        ProductVariant v4 = findOrCreateVariant(p3, "DIP500-30", "Caixa com 30 comprimidos", BigDecimal.valueOf(12.90));
        ensureProductCategory(p3, categoryMed);
        
        Product p4 = findOrCreateProduct("Sabonete Antibacteriano", "Sabonete líquido antibacteriano", "Triclosan", "Nenhuma", brand1, manufacturer1, false);
        ProductVariant v5 = findOrCreateVariant(p4, "SAB-250", "Frasco 250ml", BigDecimal.valueOf(12.50));
        ProductVariant v6 = findOrCreateVariant(p4, "SAB-500", "Frasco 500ml", BigDecimal.valueOf(22.90));
        ensureProductCategory(p4, categoryHig);
        
        Product p5 = findOrCreateProduct("Shampoo Anticaspa", "Shampoo para tratamento de caspa", "Cetoconazol", "Nenhuma", brand3, manufacturer3, false);
        ProductVariant v7 = findOrCreateVariant(p5, "SHAM-200", "Frasco 200ml", BigDecimal.valueOf(24.90));
        ensureProductCategory(p5, categoryHig);
        
        Product p6 = findOrCreateProduct("Vitamina C 1000mg", "Suplemento de vitamina C", "Ácido Ascórbico", "Nenhuma", brand2, manufacturer2, false);
        ProductVariant v8 = findOrCreateVariant(p6, "VITC-30", "Frasco com 30 comprimidos", BigDecimal.valueOf(35.90));
        ensureProductCategory(p6, categoryVit);
        
        Product p7 = findOrCreateProduct("Multivitamínico", "Complexo vitamínico completo", "Múltiplas vitaminas", "Nenhuma", brand3, manufacturer3, false);
        ProductVariant v9 = findOrCreateVariant(p7, "MULTI-60", "Frasco com 60 comprimidos", BigDecimal.valueOf(45.90));
        ensureProductCategory(p7, categoryVit);
        
        Product p8 = findOrCreateProduct("Protetor Solar FPS 50", "Protetor solar facial", "Óxido de Zinco", "Nenhuma", brand1, manufacturer1, false);
        ProductVariant v10 = findOrCreateVariant(p8, "SOLAR-60", "Frasco 60ml", BigDecimal.valueOf(42.90));
        ensureProductCategory(p8, categoryCos);
        
        Product p9 = findOrCreateProduct("Hidratante Corporal", "Creme hidratante para o corpo", "Glicerina", "Nenhuma", brand2, manufacturer2, false);
        ProductVariant v11 = findOrCreateVariant(p9, "HIDR-400", "Pote 400ml", BigDecimal.valueOf(28.90));
        ensureProductCategory(p9, categoryCos);
        
        Product p10 = findOrCreateProduct("Leite em Pó Infantil", "Fórmula infantil completa", "Proteínas do leite", "Nenhuma", brand3, manufacturer3, false);
        ProductVariant v12 = findOrCreateVariant(p10, "LEITE-800", "Lata 800g", BigDecimal.valueOf(65.90));
        ensureProductCategory(p10, categoryInf);
        
        Product p11 = findOrCreateProduct("Fralda Descartável", "Fralda descartável tamanho G", "Celulose", "Nenhuma", brand1, manufacturer1, false);
        ProductVariant v13 = findOrCreateVariant(p11, "FRALDA-G-44", "Pacote com 44 unidades", BigDecimal.valueOf(52.90));
        ensureProductCategory(p11, categoryInf);
        
        Product p12 = findOrCreateProduct("Amoxicilina 500mg", "Antibiótico de amplo espectro", "Amoxicilina", "Requer receita", brand2, manufacturer2, true);
        ProductVariant v14 = findOrCreateVariant(p12, "AMOX-21", "Caixa com 21 cápsulas", BigDecimal.valueOf(38.90));
        ensureProductCategory(p12, categoryMed);
        
        // Cria inventário (verificando se já existe)
        createInventoryIfNotExists(pharmacy1, v1, 100, BigDecimal.valueOf(15.90));
        createInventoryIfNotExists(pharmacy1, v2, 50, BigDecimal.valueOf(28.90));
        createInventoryIfNotExists(pharmacy1, v3, 80, BigDecimal.valueOf(18.50));
        createInventoryIfNotExists(pharmacy1, v4, 120, BigDecimal.valueOf(12.90));
        createInventoryIfNotExists(pharmacy1, v5, 200, BigDecimal.valueOf(12.50));
        createInventoryIfNotExists(pharmacy1, v6, 150, BigDecimal.valueOf(22.90));
        createInventoryIfNotExists(pharmacy1, v7, 90, BigDecimal.valueOf(24.90));
        createInventoryIfNotExists(pharmacy1, v8, 60, BigDecimal.valueOf(35.90));
        createInventoryIfNotExists(pharmacy1, v9, 70, BigDecimal.valueOf(45.90));
        createInventoryIfNotExists(pharmacy1, v10, 85, BigDecimal.valueOf(42.90));
        createInventoryIfNotExists(pharmacy1, v11, 110, BigDecimal.valueOf(28.90));
        createInventoryIfNotExists(pharmacy1, v12, 40, BigDecimal.valueOf(65.90));
        createInventoryIfNotExists(pharmacy1, v13, 75, BigDecimal.valueOf(52.90));
        createInventoryIfNotExists(pharmacy1, v14, 55, BigDecimal.valueOf(38.90));
        
        createInventoryIfNotExists(pharmacy2, v1, 80, BigDecimal.valueOf(14.90));
        createInventoryIfNotExists(pharmacy2, v2, 40, BigDecimal.valueOf(27.90));
        createInventoryIfNotExists(pharmacy2, v3, 70, BigDecimal.valueOf(17.50));
        createInventoryIfNotExists(pharmacy2, v4, 100, BigDecimal.valueOf(11.90));
        createInventoryIfNotExists(pharmacy2, v5, 150, BigDecimal.valueOf(11.50));
        createInventoryIfNotExists(pharmacy2, v6, 120, BigDecimal.valueOf(21.90));
        createInventoryIfNotExists(pharmacy2, v7, 75, BigDecimal.valueOf(23.90));
        createInventoryIfNotExists(pharmacy2, v8, 50, BigDecimal.valueOf(34.90));
        createInventoryIfNotExists(pharmacy2, v9, 60, BigDecimal.valueOf(44.90));
        createInventoryIfNotExists(pharmacy2, v10, 70, BigDecimal.valueOf(41.90));
        createInventoryIfNotExists(pharmacy2, v11, 95, BigDecimal.valueOf(27.90));
        createInventoryIfNotExists(pharmacy2, v12, 35, BigDecimal.valueOf(64.90));
        createInventoryIfNotExists(pharmacy2, v13, 65, BigDecimal.valueOf(51.90));
        createInventoryIfNotExists(pharmacy2, v14, 45, BigDecimal.valueOf(37.90));
        
        createInventoryIfNotExists(pharmacy3, v1, 90, BigDecimal.valueOf(16.90));
        createInventoryIfNotExists(pharmacy3, v2, 45, BigDecimal.valueOf(29.90));
        createInventoryIfNotExists(pharmacy3, v3, 75, BigDecimal.valueOf(19.50));
        createInventoryIfNotExists(pharmacy3, v4, 110, BigDecimal.valueOf(13.90));
        createInventoryIfNotExists(pharmacy3, v5, 180, BigDecimal.valueOf(13.50));
        createInventoryIfNotExists(pharmacy3, v6, 130, BigDecimal.valueOf(23.90));
        createInventoryIfNotExists(pharmacy3, v7, 85, BigDecimal.valueOf(25.90));
        createInventoryIfNotExists(pharmacy3, v8, 55, BigDecimal.valueOf(36.90));
        createInventoryIfNotExists(pharmacy3, v9, 65, BigDecimal.valueOf(46.90));
        createInventoryIfNotExists(pharmacy3, v10, 80, BigDecimal.valueOf(43.90));
        createInventoryIfNotExists(pharmacy3, v11, 105, BigDecimal.valueOf(29.90));
        createInventoryIfNotExists(pharmacy3, v12, 38, BigDecimal.valueOf(66.90));
        createInventoryIfNotExists(pharmacy3, v13, 70, BigDecimal.valueOf(53.90));
        createInventoryIfNotExists(pharmacy3, v14, 50, BigDecimal.valueOf(39.90));
        logger.info("=== SEED DE PRODUTOS E INVENTÁRIO CONCLUÍDO ===");
        logger.info("Produtos criados: {}", productRepository.count());
        logger.info("Itens de inventário criados: {}", inventoryRepository.count());
    }
    
    private Product findOrCreateProduct(String name, String description, String activePrinciple, String controlledSubstanceList, Brand brand, Manufacturer manufacturer, boolean prescriptionRequired) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> createProduct(name, description, activePrinciple, controlledSubstanceList, brand, manufacturer, prescriptionRequired));
    }
    
    private ProductVariant findOrCreateVariant(Product product, String sku, String description, BigDecimal price) {
        return productVariantRepository.findBySku(sku)
                .orElseGet(() -> createProductVariant(product, sku, description, price));
    }
    
    private void ensureProductCategory(Product product, Category category) {
        // Cria o ID composto para verificar se já existe
        ProductCategoryId categoryId = new ProductCategoryId();
        categoryId.setProductId(product.getId());
        categoryId.setCategoryId(category.getId());
        
        // Verifica diretamente no banco se a relação já existe
        if (productCategoryRepository.existsById(categoryId)) {
            return; // Já existe, não precisa criar
        }
        
        // Cria a relação
        createProductCategory(product, category);
    }
    
    private void createInventoryIfNotExists(Pharmacy pharmacy, ProductVariant variant, Integer quantity, BigDecimal price) {
        InventoryId inventoryId = new InventoryId();
        inventoryId.setPharmacyId(pharmacy.getId());
        inventoryId.setProductVariantId(variant.getId());
        
        if (!inventoryRepository.existsById(inventoryId)) {
            createInventory(pharmacy, variant, quantity, price);
        }
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }

    private User createUser(String email, String password, String fullName, Set<Role> roles) {
        User user = new User();
        user.setPublicId(UUID.randomUUID());
        user.setEmail(email);
        user.setHashedPassword(passwordEncoder.encode(password));
        // Gera número de telefone único baseado no email para evitar duplicatas
        user.setPhoneNumber(generateUniquePhoneNumber(email));
        user.setActive(true);
        user.setRoles(roles);
        return userRepository.save(user);
    }
    
    private String generateUniquePhoneNumber(String email) {
        // Gera um número único baseado no hash do email
        int hash = email.hashCode();
        // Garante número positivo e formato brasileiro (11 dígitos)
        String phone = String.format("11%09d", Math.abs(hash) % 1000000000);
        // Verifica se já existe, se sim, adiciona um sufixo
        int suffix = 1;
        String finalPhone = phone;
        while (userRepository.findByPhoneNumber(finalPhone).isPresent()) {
            finalPhone = String.format("11%08d%d", Math.abs(hash) % 100000000, suffix);
            suffix++;
        }
        return finalPhone;
    }

    private Customer createCustomer(User user, String fullName, CustomerTypeEnum type, String cpf) {
        // Garante que o User está persistido
        if (user.getId() == null) {
            user = userRepository.save(user);
        }
        
        Long userId = user.getId();
        if (userId == null) {
            throw new IllegalStateException("User ID não pode ser nulo após salvar");
        }
        
        Customer customer = new Customer();
        customer.setId(userId);
        customer.setUser(user);
        customer.setFullName(fullName);
        customer.setCustomerType(type);
        customer.setCpf(cpf);
        return customerRepository.save(customer);
    }

    private Address createAddress(String street, String city, String state, String zipCode) {
        Address address = new Address();
        address.setStreet(street);
        address.setNeighborhood("Asa Norte");
        address.setCity(city);
        address.setState(state);
        address.setCountry("Brasil");
        address.setPostalCode(zipCode);
        return addressRepository.save(address);
    }

    private Address createAddressForPharmacy(String street, String city, String state, String zipCode) {
        Address address = new Address();
        address.setStreet(street);
        address.setNeighborhood("Asa Norte");
        address.setCity(city);
        address.setState(state);
        address.setCountry("Brasil");
        address.setPostalCode(zipCode);
        return addressRepository.save(address);
    }

    private Pharmacy createPharmacy(String legalName, String tradeName, String cnpj, String phone, String email, User admin) {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setLegalName(legalName);
        pharmacy.setTradeName(tradeName);
        pharmacy.setCnpj(cnpj);
        pharmacy.setPhone(phone);
        pharmacy.setEmail(email);
        Pharmacy saved = pharmacyRepository.save(pharmacy);
        return saved;
    }


    private void createPharmacyAddress(Pharmacy pharmacy, Address address) {
        // Garante que o pharmacy está managed dentro da transação
        Pharmacy managedPharmacy = pharmacyRepository.findById(pharmacy.getId())
                .orElseThrow(() -> new IllegalStateException("Farmácia não encontrada: " + pharmacy.getId()));
        
        // Força a inicialização da coleção addresses antes de acessá-la
        Hibernate.initialize(managedPharmacy.getAddresses());
        
        PharmacyAddress pharmacyAddress = new PharmacyAddress();
        pharmacyAddress.setPharmacy(managedPharmacy);
        pharmacyAddress.setAddress(address);
        pharmacyAddress.setIsPrimary(true);
        
        // Inicializa a coleção se for null
        if (managedPharmacy.getAddresses() == null) {
            managedPharmacy.setAddresses(new HashSet<>());
        }
        managedPharmacy.getAddresses().add(pharmacyAddress);
        pharmacyRepository.save(managedPharmacy);
    }

    private DeliveryPersonnel createDeliveryPersonnel(User user, String cnh, String vehicleDetails) {
        // Garante que o User está persistido
        if (user.getId() == null) {
            user = userRepository.save(user);
        }
        
        Long userId = user.getId();
        if (userId == null) {
            throw new IllegalStateException("User ID não pode ser nulo após salvar");
        }
        
        // Verifica se já existe antes de criar
        DeliveryPersonnel existing = deliveryPersonnelRepository.findById(userId).orElse(null);
        if (existing != null) {
            return existing;
        }
        
        // Recarrega o User do banco para garantir que está managed na transação atual
        User managedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User não encontrado: " + userId));
        
        DeliveryPersonnel personnel = new DeliveryPersonnel();
        personnel.setId(userId);
        personnel.setUser(managedUser);
        personnel.setCnh(cnh);
        personnel.setVehicleDetails(vehicleDetails);
        return deliveryPersonnelRepository.save(personnel);
    }

    private Brand createBrand(String name) {
        Brand brand = new Brand();
        brand.setName(name);
        return brandRepository.save(brand);
    }

    private Manufacturer createManufacturer(String name) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(name);
        return manufacturerRepository.save(manufacturer);
    }

    private Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private Product createProduct(String name, String description, String activePrinciple, String controlledSubstanceList, Brand brand, Manufacturer manufacturer, boolean prescriptionRequired) {
        Product product = new Product();
        product.setPublicId(UUID.randomUUID());
        product.setName(name);
        product.setDescription(description);
        product.setActivePrinciple(activePrinciple);
        product.setControlledSubstanceList(controlledSubstanceList);
        product.setBrand(brand);
        product.setManufacturer(manufacturer);
        product.setPrescriptionRequired(prescriptionRequired);
        return productRepository.save(product);
    }

    private ProductVariant createProductVariant(Product product, String sku, String description, BigDecimal price) {
        // Verifica se o variant já existe pelo SKU
        ProductVariant existingVariant = productVariantRepository.findBySku(sku).orElse(null);
        if (existingVariant != null) {
            return existingVariant;
        }
        
        // Garante que o product está managed dentro da transação
        Product managedProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Produto não encontrado: " + product.getId()));
        
        // Cria e salva o variant - o relacionamento é estabelecido pelo @JoinColumn no ProductVariant
        ProductVariant variant = new ProductVariant();
        variant.setProduct(managedProduct);
        variant.setSku(sku);
        variant.setDosage("500mg");
        variant.setPackageSize(description);
        
        // Salva o variant - o Hibernate gerencia o relacionamento automaticamente
        return productVariantRepository.save(variant);
    }

    private void createProductCategory(Product product, Category category) {
        // Garante que o product e category estão managed dentro da transação
        Product managedProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Produto não encontrado: " + product.getId()));
        Category managedCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalStateException("Categoria não encontrada: " + category.getId()));
        
        // Cria o ProductCategory
        ProductCategoryId categoryId = new ProductCategoryId();
        categoryId.setProductId(managedProduct.getId());
        categoryId.setCategoryId(managedCategory.getId());
        
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(categoryId);
        productCategory.setProduct(managedProduct);
        productCategory.setCategory(managedCategory);
        
        // Salva diretamente - não precisa manipular a coleção lazy
        productCategoryRepository.save(productCategory);
    }

    private void createInventory(Pharmacy pharmacy, ProductVariant variant, Integer quantity, BigDecimal price) {
        Inventory inventory = new Inventory();
        InventoryId inventoryId = new InventoryId();
        inventoryId.setPharmacyId(pharmacy.getId());
        inventoryId.setProductVariantId(variant.getId());
        inventory.setId(inventoryId);
        inventory.setPharmacy(pharmacy);
        inventory.setProductVariant(variant);
        inventory.setQuantity(quantity);
        inventory.setPrice(price);
        inventoryRepository.save(inventory);
    }
}

