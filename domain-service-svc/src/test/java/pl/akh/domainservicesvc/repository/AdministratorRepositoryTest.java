package pl.akh.domainservicesvc.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.model.entities.Address;
import pl.akh.domainservicesvc.model.entities.Administrator;
import pl.akh.domainservicesvc.model.entities.Department;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AdministratorRepositoryTest extends DomainServiceIntegrationTest {

    @Autowired
    AdministratorRepository administratorRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    public void administratorShouldBeSave() {

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

        Department save = departmentRepository.save(department);

        UUID uuid = UUID.randomUUID();
        Administrator administrator = new Administrator();
        administrator.setFirstName("Piotr");
        administrator.setSecondName("Kowalski");
        administrator.setId(uuid);
        administrator.setDepartment(save);
        //when
        administratorRepository.findAll();
        administratorRepository.save(administrator);

        //then
        Assertions.assertEquals(1L, administratorRepository.count());
    }

}
