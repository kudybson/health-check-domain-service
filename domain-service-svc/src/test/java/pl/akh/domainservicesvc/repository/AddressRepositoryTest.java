package pl.akh.domainservicesvc.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.github.dockerjava.core.dockerfile.DockerfileStatement;
import pl.akh.domainservicesvc.model.entities.Address;
import pl.akh.utils.AddressValidator;

public class AddressRepositoryTest {

    @Autowired
    AddressRepository addressRepository;

    @Test
    @Transactional
    public void addressShouldBeSave() {
        Address address = new Address();
        address.setCountry("PL");
        address.setPostalCode("12-123");
        address.setPost("Krakow Lobzow");
        address.setProvince("Malopolskie");
        address.setCounty("krakowski");
        address.setCity("Krakow");
        address.setStreet("Wybickiego");
        address.setApartmentNumber("106");
        address.setHouseNumber("56");

        addressRepository.save(address);

        Example<Address> x = Example.of(address);

        //Assertions.assertEquals(1L, addressRepository.count());
        Assertions.assertTrue(addressRepository.exists(x));
    }
}
