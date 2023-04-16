package pl.akh.domainservicesvc.domain.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaSystemException;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.Address;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.Receptionist;

import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReceptionistRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Test
    public void shouldCreateReceptionist() {
        //given
        UUID id = UUID.randomUUID();
        Department department = getSavedDepartment();
        Receptionist receptionist = createReceptionist(id, "Anna", "Nowak", department);

        //when
        receptionistRepository.saveAndFlush(receptionist);

        //then
        Optional<Receptionist> byId = receptionistRepository.findById(id);
        Receptionist savedReceptionist = byId.orElseThrow();
        assertNotNull(savedReceptionist.getFirstName());
        assertNotNull(savedReceptionist.getLastName());
        assertNotNull(savedReceptionist.getDepartment());
        Assertions.assertEquals(savedReceptionist.getDepartment(), department);
    }

    @Test
    public void shouldNotCreateReceptionistIfIdIsNull() {
        //given
        Department department = getSavedDepartment();
        Receptionist receptionist = createReceptionist(null, "Anna", "Nowak", department);

        //when
        //then
        assertThrows(JpaSystemException.class, () -> {
            receptionistRepository.save(receptionist);
            receptionistRepository.flush();
        });
    }

    @Test
    public void shouldNotCreateReceptionistIfDepartmentIsNull() {
        //given
        UUID id = UUID.randomUUID();
        Receptionist receptionist = createReceptionist(id, "Anna", "Nowak", null);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            receptionistRepository.save(receptionist);
            receptionistRepository.flush();
        });
    }

    @Test
    public void shouldNotCreateReceptionistIfLastNameIsEmpty() {
        //given
        UUID id = UUID.randomUUID();
        Department department = getSavedDepartment();
        Receptionist receptionist = createReceptionist(id, "", "Nowak", department);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            receptionistRepository.save(receptionist);
            receptionistRepository.flush();
        });
    }

    @Test
    public void shouldDeleteReceptionistWithoutDeleteDepartment() {
        //given
        UUID id = UUID.randomUUID();
        Department department = getSavedDepartment();
        Receptionist receptionist = createReceptionist(id, "Anna", "Nowak", department);

        //when
        Receptionist save = receptionistRepository.save(receptionist);
        receptionistRepository.flush();
        receptionistRepository.deleteById(id);

        //then
        Optional<Receptionist> receptionistOptional = receptionistRepository.findById(id);
        assertTrue(receptionistOptional.isEmpty());
        Optional<Department> departmentOptional = departmentRepository.findById(department.getId());
        assertTrue(departmentOptional.isPresent());
        Department departmentAfterDelete = departmentOptional.get();
        assertFalse(departmentAfterDelete.getReceptionists().contains(save));
    }


    @Test
    public void shouldNotCascadeCreateDepartment() {
        //given
        UUID id = UUID.randomUUID();
        Receptionist receptionist = createReceptionist(id, "Anna", "Nowak", prepareDepartment());

        //when
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            receptionistRepository.save(receptionist);
            receptionistRepository.flush();
        });
    }

    private Receptionist createReceptionist(UUID id, String firstName, String lastName, Department department) {
        Receptionist receptionist = new Receptionist();
        receptionist.setId(id);
        receptionist.setDepartment(department);
        receptionist.setFirstName(firstName);
        receptionist.setLastName(lastName);
        return receptionist;
    }

    private Department getSavedDepartment() {
        return departmentRepository.saveAndFlush(prepareDepartment());
    }

    private Department prepareDepartment() {
        Department department = new Department();
        department.setName("ReceptionistRepositoryIT department");
        department.setAddress(prepareAddress());
        return department;
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
