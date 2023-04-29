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
import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.domainservicesvc.domain.model.entities.enums.Specialization;

import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.akh.domainservicesvc.domain.model.entities.enums.Specialization.ANESTHESIA;

public class DoctorRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    public void shouldCreateDoctor() {
        //given
        UUID id = UUID.randomUUID();
        Department attatchedDepartment = getAttatchedDepartment();
        Doctor doctor = createDoctor(id, "First", "Last", ANESTHESIA, attatchedDepartment);

        //when
        doctorRepository.saveAndFlush(doctor);
        //then
        Optional<Doctor> byId = doctorRepository.findById(id);
        Doctor savedDoctor = byId.orElseThrow();
        assertNotNull(savedDoctor.getFirstName());
        assertNotNull(savedDoctor.getLastName());

        assertNotNull(savedDoctor.getDepartment());
        Assertions.assertEquals(savedDoctor.getDepartment(), attatchedDepartment);
    }

    @Test
    public void shouldNotCreateDoctorIfIdIsNull() {
        //given
        Department attatchedDepartment = getAttatchedDepartment();
        Doctor doctor = createDoctor(null, "First", "Last", ANESTHESIA, attatchedDepartment);

        //when
        assertThrows(JpaSystemException.class, () -> {
            doctorRepository.save(doctor);
            doctorRepository.flush();
        });
    }

    @Test
    public void shouldNotCreateDoctorIfDepartmentIsNull() {
        //given
        UUID id = UUID.randomUUID();
        Doctor doctor = createDoctor(id, "First", "Last", ANESTHESIA, null);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            doctorRepository.save(doctor);
            doctorRepository.flush();
        });
    }

    @Test
    public void shouldNotCreateDoctorIfFirstNameIsEmpty() {
        //given
        UUID id = UUID.randomUUID();
        Department attatchedDepartment = getAttatchedDepartment();
        Doctor doctor = createDoctor(id, "", "Last", ANESTHESIA, attatchedDepartment);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            doctorRepository.save(doctor);
            doctorRepository.flush();
        });
    }

    @Test
    public void shouldNotCreateDoctorIfSpecializationIsNull() {
        //when
        //then
        UUID id = UUID.randomUUID();
        Department attatchedDepartment = getAttatchedDepartment();
        Doctor doctor = createDoctor(id, "First", "Last", null, attatchedDepartment);


        //when
        assertThrows(ConstraintViolationException.class, () -> {
            doctorRepository.save(doctor);
            doctorRepository.flush();
        });
    }

    @Test
    public void shouldDeleteDoctorWithoutAffectingDepartment() {
        //when
        //then
        UUID id = UUID.randomUUID();
        Department attatchedDepartment = getAttatchedDepartment();
        Doctor doctor = createDoctor(id, "First", "Last", ANESTHESIA, attatchedDepartment);

        //when
        Doctor save = doctorRepository.save(doctor);
        doctorRepository.flush();
        doctorRepository.deleteById(id);
        //then
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        assertTrue(doctorOptional.isEmpty());
        Optional<Department> departmentOptional = departmentRepository.findById(attatchedDepartment.getId());
        assertTrue(departmentOptional.isPresent());
        Department departmentAfterDelete = departmentOptional.get();
        assertFalse(departmentAfterDelete.getDoctors().contains(save));
    }

    @Test
    public void shouldNotCascadeCreateDepartment() {
        //given
        UUID id = UUID.randomUUID();
        Doctor doctor = createDoctor(id, "First", "Last", ANESTHESIA, prepareDepartment());

        //when
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            doctorRepository.save(doctor);
            doctorRepository.flush();
        });
    }

    private Doctor createDoctor(UUID id, String firstName, String lastName, Specialization specialization, Department department) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setDepartment(department);
        doctor.setSpecialization(specialization);
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        return doctor;
    }

    private Department getAttatchedDepartment() {
        return departmentRepository.saveAndFlush(prepareDepartment());
    }

    private Department prepareDepartment() {
        Department department = new Department();
        department.setName("DoctorRepositoryIT department");
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
