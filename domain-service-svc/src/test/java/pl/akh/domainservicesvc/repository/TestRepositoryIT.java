package pl.akh.domainservicesvc.repository;

import pl.akh.domainservicesvc.model.entities.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.model.entities.Address;
import pl.akh.domainservicesvc.model.entities.Department;
import pl.akh.domainservicesvc.model.entities.Gender;
import pl.akh.domainservicesvc.model.entities.Patient;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TestRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PatientRepository patientRepository;


    @org.junit.jupiter.api.Test
    public void shouldCreateTest() {
        //TODO
    }

    private Patient createPatient() {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setFirstName("John");
        patient.setSecondName("Wick");
        patient.setGender(Gender.MALE);
        patient.setPhoneNumber("797634917");
        patient.setPesel("00210147133");
        patient.setAddress(prepareAddress());
        return patientRepository.saveAndFlush(patient);
    }

    private Department prepareDepartment() {
        Department department = new Department();
        department.setName("RatingRepositoryIT department");
        department.setAddress(prepareAddress());
        return departmentRepository.saveAndFlush(department);
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
