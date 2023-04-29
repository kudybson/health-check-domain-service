package pl.akh.domainservicesvc.domain.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.*;
import pl.akh.domainservicesvc.domain.model.entities.enums.Gender;
import pl.akh.domainservicesvc.domain.model.entities.enums.TestResultStatus;
import pl.akh.domainservicesvc.domain.model.entities.enums.TestType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;


@ExtendWith(MockitoExtension.class)
public class MedicalMedicalTestRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private MedicalTestRepository medicalTestRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    private final Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    private final Timestamp nowMinusOneDay = Timestamp.valueOf(now.toLocalDateTime().minusDays(1));
    private final Timestamp nowMinusTwoDays = Timestamp.valueOf(now.toLocalDateTime().minusDays(2));
    private final Timestamp nowPlusOneDay = Timestamp.valueOf(now.toLocalDateTime().plusDays(1));
    private final Timestamp nowPlusTwoDays = Timestamp.valueOf(now.toLocalDateTime().plusDays(2));

    @Test
    public void shouldCreateTest() {
        //given
        Patient patient = preparePatient();
        Department department = prepareDepartment();
        MedicalTest test = createTest(patient, department, TestType.BLOOD_ALLERGY_TESTS, now);

        //when
        MedicalTest save = medicalTestRepository.save(test);

        //then
        assertNotNull(save);
    }

    @Test
    public void shouldNotCreateMedicalTestWhenPatientIsNull() {
        //given
        Department department = prepareDepartment();
        MedicalTest test = createTest(null, department, TestType.BLOOD_ALLERGY_TESTS, now);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            medicalTestRepository.saveAndFlush(test);
        });
    }

    @Test
    public void shouldNotCreateMedicalTestWhenDepartmentIsNull() {
        //given
        Patient patient = preparePatient();
        Department department = prepareDepartment();
        MedicalTest test = createTest(patient, null, TestType.BLOOD_ALLERGY_TESTS, now);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            medicalTestRepository.saveAndFlush(test);
        });
    }

    @Test
    public void shouldNotCreateMedicalTestWhenTypeIsNull() {
        //given
        Patient patient = preparePatient();
        Department department = prepareDepartment();
        MedicalTest test = createTest(patient, department, null, now);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            medicalTestRepository.saveAndFlush(test);
        });
    }

    @Test
    public void shouldNotCreateMedicalTestWhenDateIsNull() {
        //given
        Patient patient = preparePatient();
        Department department = prepareDepartment();
        MedicalTest test = createTest(patient, department, TestType.HARMONY_TEST, null);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            medicalTestRepository.saveAndFlush(test);
        });
    }

    @Test
    public void shouldReturnMedicalTestByDepartmentIdAndTypeAndBetweenDates() {
        //given
        Patient patient = preparePatient();
        Department department = prepareDepartment();
        Department department2 = prepareDepartment();
        List<MedicalTest> test = List.of(
                createTest(patient, department, TestType.HARMONY_TEST, now),
                createTest(patient, department2, TestType.HARMONY_TEST, now),
                createTest(patient, department, TestType.ALLERGY_SKIN_TESTS, now),
                createTest(patient, department, TestType.HARMONY_TEST, nowMinusTwoDays),
                createTest(patient, department, TestType.HARMONY_TEST, nowPlusTwoDays));

        medicalTestRepository.saveAll(test);

        //when
        List<MedicalTest> allByDepartmentIdAndType = medicalTestRepository.findAllByDepartmentIdAndType(department.getId(), TestType.HARMONY_TEST, nowMinusOneDay, nowPlusOneDay);

        //then
        Assert.assertEquals(1, allByDepartmentIdAndType.size());
        MedicalTest medicalTest = allByDepartmentIdAndType.stream().findAny().orElseThrow();
        assertEquals(medicalTest.getDepartment(), department);
        assertEquals(medicalTest.getPatient(), patient);
        assertEquals(medicalTest.getType(), TestType.HARMONY_TEST);
        assertEquals(medicalTest.getTestDate(), now);
    }

    @Test
    public void shouldReturnMedicalTestsByPatientID() {
        //given
        Patient patient = preparePatient();
        Patient patient2 = preparePatient();
        Department department = prepareDepartment();
        List<MedicalTest> test = List.of(
                createTest(patient, department, TestType.HARMONY_TEST, now),
                createTest(patient2, department, TestType.ALLERGY_SKIN_TESTS, now));

        medicalTestRepository.saveAll(test);

        //when
        Collection<MedicalTest> medicalTestsByPatientUUID = medicalTestRepository.findMedicalTestsByPatientUUID(patient.getId());

        //then
        Assertions.assertEquals(1, medicalTestsByPatientUUID.size());
        Optional<MedicalTest> first = medicalTestsByPatientUUID.stream().findFirst();
        Assertions.assertTrue(first.isPresent());
        Assertions.assertEquals(first.get().getPatient().getId(), patient.getId());
    }

    @Test
    public void shouldReturnMedicalTestDatesByDepartmentIdAndTypeAndBetweenDates() {
        //given
        Patient patient = preparePatient();
        Department department = prepareDepartment();
        Department department2 = prepareDepartment();
        List<MedicalTest> test = List.of(
                createTest(patient, department, TestType.HARMONY_TEST, now),
                createTest(patient, department2, TestType.HARMONY_TEST, now),
                createTest(patient, department, TestType.ALLERGY_SKIN_TESTS, now),
                createTest(patient, department, TestType.HARMONY_TEST, nowMinusTwoDays),
                createTest(patient, department, TestType.HARMONY_TEST, nowPlusTwoDays));

        medicalTestRepository.saveAll(test);

        //when
        List<Timestamp> allByDepartmentIdAndType = medicalTestRepository.findScheduleDatesBetween(department.getId(), TestType.HARMONY_TEST, nowMinusOneDay, nowPlusOneDay);

        //then
        Assert.assertEquals(1, allByDepartmentIdAndType.size());
        assertEquals(allByDepartmentIdAndType.get(0).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS), now.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void shouldReturnMedicalTestByDepartmentAndTypeAndDate() {
        //given
        Patient patient = preparePatient();
        Department department = prepareDepartment();
        TestType type = TestType.HARMONY_TEST;
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        MedicalTest test = createTest(patient, department, type, timestamp);

        MedicalTest save = medicalTestRepository.save(test);

        //when
        Optional<MedicalTest> byId = medicalTestRepository.findMedicalTestByDepartmentIdAndTypeAndTestDateEquals(department.getId(), type, timestamp);

        //then
        assertTrue(byId.isPresent());
        assertEquals(byId.get().getTestDate(), timestamp);
    }

    private MedicalTest createTest(Patient patient, Department department, TestType type, Timestamp timestamp) {
        MedicalTest medicalTest = new MedicalTest();
        medicalTest.setPatient(patient);
        medicalTest.setTestDate(timestamp);
        medicalTest.setDepartment(department);
        medicalTest.setType(type);
        medicalTest.setResultReady(TestResultStatus.WAITING_FOR_RESULT);
        return medicalTest;
    }

    private Patient preparePatient() {
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
