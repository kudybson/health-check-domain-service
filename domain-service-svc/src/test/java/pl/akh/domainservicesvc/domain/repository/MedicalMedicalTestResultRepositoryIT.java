package pl.akh.domainservicesvc.domain.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static pl.akh.domainservicesvc.domain.model.entities.TestType.BLOOD_ALLERGY_TESTS;

public class MedicalMedicalTestResultRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicalTestRepository medicalTestRepository;
    @Autowired
    private MedicalTestResultRepository medicalTestResultRepository;

    @Test
    public void shouldCreateTestResult() {
        //given
        MedicalTest medicalTest = createMedicalTest();
        MedicalTestResult medicalTestResult = createTestResult(medicalTest, "podwyższone krwinki czerwone");

        //when
        medicalTestResultRepository.saveAndFlush(medicalTestResult);

        //then
        Assertions.assertEquals(1, medicalTestResultRepository.findAll().size());
    }

    @Test
    public void shouldDeleteTestResult() {
        //given
        MedicalTest medicalTest = createMedicalTest();
        MedicalTestResult medicalTestResult = createTestResult(medicalTest, "podwyższone krwinki czerwone");

        //when
        MedicalTestResult save = medicalTestResultRepository.saveAndFlush(medicalTestResult);
        medicalTestResultRepository.delete(save);

        //then
        Assertions.assertEquals(0, medicalTestResultRepository.findAll().size());
    }

    @Test
    public void shouldNotCreateTestResultWhenMedicalTestIsNull() {
        //given
        MedicalTestResult medicalTestResult = createTestResult(null, "podwyższone krwinki czerwone");

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            medicalTestResultRepository.saveAndFlush(medicalTestResult);
        });
    }

    private MedicalTestResult createTestResult(MedicalTest medicalTest, String description) {
        MedicalTestResult medicalTestResult = new MedicalTestResult();
        medicalTestResult.setMedicalTest(medicalTest);
        medicalTestResult.setDescription(description);
        return medicalTestResult;
    }

    private MedicalTest createMedicalTest() {
        MedicalTest medicalTest = new MedicalTest();
        medicalTest.setDepartment(prepareDepartment());
        medicalTest.setMedicalTestResult(null);
        medicalTest.setPatient(createPatient());
        medicalTest.setType(BLOOD_ALLERGY_TESTS);
        medicalTest.setTestDate(Timestamp.from(Instant.now()));
        medicalTest.setDescription("test alergiczny krwi");
        return medicalTestRepository.saveAndFlush(medicalTest);
    }

    private Patient createPatient() {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID());
        patient.setFirstName("John");
        patient.setLastName("Wick");
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
