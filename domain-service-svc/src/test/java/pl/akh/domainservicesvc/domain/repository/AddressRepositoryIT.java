package pl.akh.domainservicesvc.domain.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.Address;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AddressRepositoryIT extends DomainServiceIntegrationTest {
    @Autowired
    AddressRepository addressRepository;

    @Test
    public void addressShouldBeSaved() {

        //given
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

        //when
        Address save = addressRepository.save(address);
        Optional<Address> byId = addressRepository.findById(save.getId());

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void addressShouldBeDeleted() {

        //given
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

        //when
        Address saved = addressRepository.save(address);
        addressRepository.delete(saved);

        //then
        assertTrue(addressRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    public void addressShouldBeUpdated() {

        //given
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

        //when
        Address saved = addressRepository.save(address);
        saved.setProvince("Lubelskie");
        addressRepository.save(saved);

        //then
        assertTrue(addressRepository.findById(saved.getId()).isPresent());
        Assertions.assertEquals(saved.getProvince(), "Lubelskie");
    }

    @Test
    public void addressShouldNotBeSavedBecauseCityIsNull() {

        //given
        Address address = new Address();
        address.setCountry("PL");
        address.setPostalCode("12-123");
        address.setPost("Krakow Lobzow");
        address.setProvince("Malopolskie");
        address.setCounty("krakowski");
        address.setStreet("Wybickiego");
        address.setApartmentNumber("106");
        address.setHouseNumber("56");


        assertThrows(ConstraintViolationException.class, () -> {
            addressRepository.save(address);
            addressRepository.flush();
        });
    }

    @Test
    public void addressShouldNotBeSavedBecauseCityIsEmpty() {

        //given
        Address address = new Address();
        address.setCity("");
        address.setCountry("PL");
        address.setPostalCode("12-123");
        address.setPost("Krakow Lobzow");
        address.setProvince("Malopolskie");
        address.setCounty("krakowski");
        address.setStreet("Wybickiego");
        address.setApartmentNumber("106");
        address.setHouseNumber("56");


        assertThrows(ConstraintViolationException.class, () -> {
            addressRepository.save(address);
            addressRepository.flush();
        });
    }

}
