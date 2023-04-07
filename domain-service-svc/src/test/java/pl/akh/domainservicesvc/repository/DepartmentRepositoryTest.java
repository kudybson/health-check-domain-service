package pl.akh.domainservicesvc.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.model.entities.Address;
import pl.akh.domainservicesvc.model.entities.Department;

@ExtendWith(MockitoExtension.class)
public class DepartmentRepositoryTest extends DomainServiceIntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    public void addressShouldBeDeleteWithDepartment() {

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
        addressRepository.save(address);
        Department saveDepartment = departmentRepository.save(department);
        departmentRepository.delete(saveDepartment);

        //then
        Assertions.assertEquals(0L, addressRepository.count());
        Assertions.assertEquals(0L, departmentRepository.count());
    }
}
