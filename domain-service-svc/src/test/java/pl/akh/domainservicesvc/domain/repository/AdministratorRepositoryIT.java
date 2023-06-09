package pl.akh.domainservicesvc.domain.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.Address;
import pl.akh.domainservicesvc.domain.model.entities.Administrator;
import pl.akh.domainservicesvc.domain.model.entities.Department;

import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class AdministratorRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    AdministratorRepository administratorRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    public void administratorShouldBeSave() {
        Department save = prepareDepartment();
        //given

        UUID uuid = UUID.randomUUID();
        Administrator administrator = new Administrator();
        administrator.setFirstName("Piotr");
        administrator.setLastName("Kowalski");
        administrator.setId(uuid);
        administrator.setDepartment(save);

        //when
        Administrator savedAdmin = administratorRepository.save(administrator);

        //then
        assertTrue(administratorRepository.findById(savedAdmin.getId()).isPresent());
        assertNotNull(savedAdmin.getDepartment());
    }

    @Test
    public void adminShouldBeDetachedFromDepartment() {
        Department save = prepareDepartment();

        UUID uuid = UUID.randomUUID();
        Administrator administrator = new Administrator();
        administrator.setFirstName("Piotr");
        administrator.setLastName("Kowalski");
        administrator.setId(uuid);
        administrator.setDepartment(save);

        //when
        Administrator savedAdmin = administratorRepository.save(administrator);

        //then
        assertTrue(administratorRepository.findById(savedAdmin.getId()).isPresent());
        assertNotNull(savedAdmin.getDepartment());
        assertNotNull(savedAdmin.getDepartment().getAddress());
        assertNull(savedAdmin.getDepartment().getAdministrator());

        administratorRepository.delete(savedAdmin);
        assertTrue(administratorRepository.findById(savedAdmin.getId()).isEmpty());
        assertTrue(departmentRepository.findById(savedAdmin.getDepartment().getId()).isPresent());


    }

    private Department prepareDepartment() {
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

        department = departmentRepository.save(department);
        assertNotNull(department);
        return department;
    }

}
