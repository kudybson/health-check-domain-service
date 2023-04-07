package pl.akh.domainservicesvc.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.model.entities.Address;
import pl.akh.domainservicesvc.model.entities.Department;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AddressRepositoryTest extends DomainServiceIntegrationTest {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    public void addressShouldBeSave() {

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
    public void addressShouldBeDelete() {

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
        addressRepository.delete(save);

        //then
        Assertions.assertEquals(0L, addressRepository.count());
    }

    @Test
    public void addressShouldBeUpdate() {

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
        save.setProvince("Lubelskie");
        addressRepository.save(save);

        //then
        Assertions.assertEquals(save.getProvince(), "Lubelskie");
        Assertions.assertEquals(1L, addressRepository.count());
    }

    @Test
    public void addressShouldNotBeDeleteAnd() {

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

        Department department = new Department();
        department.setName("Opieka medyczna");
        department.setAddress(address);

        //when
        Address save = addressRepository.save(address);
        departmentRepository.save(department);
        addressRepository.delete(save);

        //then
        Assertions.assertEquals(1L, addressRepository.count());
        Assertions.assertEquals(1L, departmentRepository.count());
    }
}
