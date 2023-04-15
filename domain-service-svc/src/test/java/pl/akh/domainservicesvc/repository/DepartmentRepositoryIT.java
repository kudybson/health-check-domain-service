package pl.akh.domainservicesvc.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.model.entities.Address;
import pl.akh.domainservicesvc.model.entities.Department;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DepartmentRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    public void addressShouldBeDeleteWithDepartment() {

        //given
        Address address = prepareAddress();

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

    @Test
    public void addressShouldNotBeDeleteAnd() {

        //given
        Address address = prepareAddress();

        Department department = new Department();
        department.setName("Opieka medyczna");
        department.setAddress(address);

        //when
        Department savedDepartment = departmentRepository.save(department);
        Address saved = savedDepartment.getAddress();
        assertNotNull(saved);

        addressRepository.delete(saved);

        //then
        Assertions.assertEquals(1L, addressRepository.count());
        Assertions.assertEquals(1L, departmentRepository.count());
    }

    @Test
    public void addressShouldBeSavedByCascade() {

        //given
        Address address = prepareAddress();

        Department department = new Department();
        department.setName("Opieka medyczna");
        department.setAddress(address);

        //when
        Department saveDepartment = departmentRepository.save(department);

        //then
        assertNotNull(saveDepartment);
        assertNotNull(saveDepartment.getAddress());
        Assertions.assertEquals(1L, addressRepository.count());
        Assertions.assertEquals(1L, departmentRepository.count());
    }

    @Test
    public void addressShouldCountAll() {
        Assertions.assertEquals(0L, addressRepository.count());
        Assertions.assertEquals(0L, departmentRepository.count());
    }

    @Test
    public void addressShouldThrowsInConstraintViolationExceptionWhenNameIsEmpty() {

        //given
        Address address = prepareAddress();
        Department department = new Department();
        department.setAddress(address);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            departmentRepository.save(department);
            departmentRepository.flush();
        });
    }

    @Test
    public void addressShouldThrowsInConstraintViolationExceptionWhenAddressIsNull() {

        //given
        Department department = new Department();
        department.setName("Name");
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            departmentRepository.save(department);
            departmentRepository.flush();
        });
    }

    private Address prepareAddress() {
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
        return address;
    }
}
