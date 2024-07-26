package projectjava.belajar.resfullapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import projectjava.belajar.resfullapi.entity.Address;
import projectjava.belajar.resfullapi.entity.Contact;
import projectjava.belajar.resfullapi.entity.User;
import projectjava.belajar.resfullapi.model.AddressResponse;
import projectjava.belajar.resfullapi.model.CreateAddressRequest;
import projectjava.belajar.resfullapi.repository.AddressRepository;
import projectjava.belajar.resfullapi.repository.ContactRepository;
import projectjava.belajar.resfullapi.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ValidationService validationService;

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validation(request);
        Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setProvince(request.getProvince());
        address.setPostalCode(request.getPostalCode());
        addressRepository.save(address);

        return toAddressResponse(address);
    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .country(address.getCountry())
                .province(address.getProvince())
                .postalCode(address.getPostalCode())
                .build();
    }

}
