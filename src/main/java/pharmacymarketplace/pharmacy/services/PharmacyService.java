package pharmacymarketplace.pharmacy.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.AlreadyExistsException;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.pharmacy.PharmacyMapper;
import pharmacymarketplace.pharmacy.domain.jpa.Pharmacy;
import pharmacymarketplace.pharmacy.domain.jpa.PharmacyAddress;
import pharmacymarketplace.pharmacy.dtos.CreatePharmacyRequest;
import pharmacymarketplace.pharmacy.dtos.PharmacyDto;
import pharmacymarketplace.pharmacy.repository.jpa.PharmacyRepository;
import pharmacymarketplace.user.domain.jpa.Address;
import pharmacymarketplace.user.repository.jpa.AddressRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final AddressRepository addressRepository;
    private final PharmacyMapper pharmacyMapper;

    public PharmacyDto findById(Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmácia não encontrada"));
        return pharmacyMapper.toDto(pharmacy);
    }

    public List<PharmacyDto> findAll() {
        return pharmacyRepository.findAll().stream()
                .map(pharmacyMapper::toDto)
                .toList();
    }

    public PharmacyDto findByEmail(String email) {
        Pharmacy pharmacy = pharmacyRepository.findByEmail(email).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Farmácia não encontrada para o email: " + email));
        return pharmacyMapper.toDto(pharmacy);
    }

    @Transactional
    public PharmacyDto createPharmacy(CreatePharmacyRequest request) {
        if (pharmacyRepository.findByCnpj(request.cnpj()).isPresent()) {
            throw new AlreadyExistsException("CNPJ já cadastrado");
        }

        if (pharmacyRepository.findByLegalName(request.legalName()).isPresent()) {
            throw new AlreadyExistsException("Razão social já cadastrada");
        }

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setLegalName(request.legalName());
        pharmacy.setTradeName(request.tradeName());
        pharmacy.setCnpj(request.cnpj());
        pharmacy.setPhone(request.phone());
        pharmacy.setEmail(request.email());
        pharmacy.setWebsite(request.website());

        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);

        // Criar endereço
        Address address = new Address();
        address.setStreet(request.address().street());
        address.setNeighborhood(request.address().neighborhood());
        address.setCity(request.address().city());
        address.setState(request.address().state());
        address.setCountry(request.address().country());
        address.setPostalCode(request.address().postalCode());
        address.setComplement(request.address().complement());
        Address savedAddress = addressRepository.save(address);

        PharmacyAddress pharmacyAddress = new PharmacyAddress();
        pharmacyAddress.setPharmacy(savedPharmacy);
        pharmacyAddress.setAddress(savedAddress);
        pharmacyAddress.setIsPrimary(true);
        pharmacyAddress.setOpeningHours(request.address().openingHours());
        savedPharmacy.getAddresses().add(pharmacyAddress);

        return pharmacyMapper.toDto(pharmacyRepository.save(savedPharmacy));
    }

    @Transactional
    public PharmacyDto updatePharmacy(Long id, CreatePharmacyRequest request) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmácia não encontrada"));

        pharmacy.setTradeName(request.tradeName());
        pharmacy.setPhone(request.phone());
        pharmacy.setEmail(request.email());
        pharmacy.setWebsite(request.website());

        return pharmacyMapper.toDto(pharmacyRepository.save(pharmacy));
    }

    @Transactional
    public void deletePharmacy(Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmácia não encontrada"));
        pharmacyRepository.delete(pharmacy);
    }
}

