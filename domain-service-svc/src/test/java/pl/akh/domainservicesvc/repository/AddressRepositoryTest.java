package pl.akh.domainservicesvc.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.model.entities.Address;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AddressRepositoryTest extends DomainServiceIntegrationTest {
    @Autowired
    AddressRepository addressRepository;

    @Test
    public void addressShouldBeSave() {
        Address address = new Address();
        address.setCity("Krakow");
        address.setCountry("PL");
        address.setPostalCode("12-123");
        address.setPost("Krakow Lobzow");
        address.setProvince("Malopolskie");
        address.setCounty("krakowski");
        address.setStreet("Wybickiego");
        address.setApartmentNumber("106");
        address.setHouseNumber("56");

        List<Address> all = addressRepository.findAll();
        Address save = addressRepository.save(address);

        Optional<Address> byId = addressRepository.findById(save.getId());

        assertNotNull(byId.get());
        //Assertions.assertEquals(1L, addressRepository.count());
    }
}
