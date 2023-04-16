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

public class TestResultRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicalTestRepository medicalTestRepository;
    @Autowired
    private TestResultRepository testResultRepository;

    @Test
    public void shouldCreateTestResult() {
        //given
        MedicalTest medicalTest = createMedicalTest();
        TestResult testResult = createTestResult(medicalTest, "podwyższone krwinki czerwone");

        //when
        testResultRepository.saveAndFlush(testResult);

        //then
        Assertions.assertEquals(1, testResultRepository.findAll().size());
    }

    @Test
    public void shouldDeleteTestResult() {
        //given
        MedicalTest medicalTest = createMedicalTest();
        TestResult testResult = createTestResult(medicalTest, "podwyższone krwinki czerwone");

        //when
        TestResult save = testResultRepository.saveAndFlush(testResult);
        testResultRepository.delete(save);

        //then
        Assertions.assertEquals(0, testResultRepository.findAll().size());
    }

    @Test
    public void shouldNotCreateTestResultWhenMedicalTestIsNull() {
        //given
        TestResult testResult = createTestResult(null, "podwyższone krwinki czerwone");

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            testResultRepository.saveAndFlush(testResult);
        });
    }

    private TestResult createTestResult(MedicalTest medicalTest, String description) {
        TestResult testResult = new TestResult();
        testResult.setMedicalTest(medicalTest);
        testResult.setDescription(description);
        return testResult;
    }

    private MedicalTest createMedicalTest() {
        MedicalTest medicalTest = new MedicalTest();
        medicalTest.setDepartment(prepareDepartment());
        medicalTest.setTestResult(null);
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
