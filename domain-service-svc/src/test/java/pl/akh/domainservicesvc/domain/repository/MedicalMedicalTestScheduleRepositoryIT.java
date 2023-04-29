package pl.akh.domainservicesvc.domain.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.Address;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTestSchedule;
import pl.akh.domainservicesvc.domain.model.entities.enums.TestType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static pl.akh.domainservicesvc.domain.model.entities.enums.TestType.ARTHROSCOPY;
import static pl.akh.domainservicesvc.domain.model.entities.enums.TestType.BLOOD_ALLERGY_TESTS;

public class MedicalMedicalTestScheduleRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MedicalTestScheduleRepository medicalTestScheduleRepository;

    @Test
    public void shouldCreateTestSchedule() {
        //given
        Department department = createDepartment();
        MedicalTestSchedule medicalTestSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        MedicalTestSchedule saved = medicalTestScheduleRepository.saveAndFlush(medicalTestSchedule);

        //then
        Optional<MedicalTestSchedule> byId = medicalTestScheduleRepository.findById(saved.getId());
        assertTrue(byId.isPresent());
        assertSchedules(medicalTestSchedule, byId.get());
    }

    @Test
    public void shouldReturnOnlySchedulesByDepartmentTypeAndDate() {
        //given
        Department department = createDepartment();
        Department department2 = createDepartment();
        MedicalTestSchedule badTypeSchedule = createTestSchedule(department, ARTHROSCOPY,
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 16, 16, 0)));
        MedicalTestSchedule startAfterEnd = createTestSchedule(department, BLOOD_ALLERGY_TESTS,
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 17, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 17, 16, 0)));
        MedicalTestSchedule endBeforeStart = createTestSchedule(department, BLOOD_ALLERGY_TESTS,
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 15, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 15, 16, 0)));
        MedicalTestSchedule wrongDepartment = createTestSchedule(department2, BLOOD_ALLERGY_TESTS,
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 16, 16, 0)));
        MedicalTestSchedule shouldBeFound = createTestSchedule(department, BLOOD_ALLERGY_TESTS,
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 16, 16, 0)));

        //when
        medicalTestScheduleRepository.saveAll(
                List.of(badTypeSchedule, startAfterEnd, endBeforeStart, wrongDepartment, shouldBeFound)
        );

        //then
        Collection<MedicalTestSchedule> medicalTestSchedules = medicalTestScheduleRepository.findByDepartmentAndTypeBetweenDates(department.getId(), BLOOD_ALLERGY_TESTS,
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2022, 4, 16, 16, 0)));

        assertTrue(medicalTestSchedules.stream().findFirst().isPresent());
        Assertions.assertEquals(shouldBeFound, medicalTestSchedules.stream().findFirst().get());
    }

    @Test
    public void shouldDeleteTestSchedule() {
        //given
        Department department = createDepartment();
        MedicalTestSchedule medicalTestSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        MedicalTestSchedule saved = medicalTestScheduleRepository.saveAndFlush(medicalTestSchedule);
        medicalTestScheduleRepository.delete(saved);

        //then
        Optional<MedicalTestSchedule> byId = medicalTestScheduleRepository.findById(saved.getId());
        assertTrue(byId.isEmpty());
    }

    @Test
    public void shouldNotCreateTestScheduleWhenDepartmentIsNull() {
        //given
        MedicalTestSchedule medicalTestSchedule = createTestSchedule(null, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            medicalTestScheduleRepository.saveAndFlush(medicalTestSchedule);
        });
    }

    @Test
    public void shouldNotCreateTestScheduleWhenTestTypeIsNull() {
        //given
        Department department = createDepartment();
        MedicalTestSchedule medicalTestSchedule = createTestSchedule(department, null, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            medicalTestScheduleRepository.saveAndFlush(medicalTestSchedule);
        });
    }

    @Test
    public void shouldNotCreateTestScheduleWhenStartDateTimeIsNull() {
        //given
        Department department = createDepartment();
        MedicalTestSchedule medicalTestSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                null);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            medicalTestScheduleRepository.saveAndFlush(medicalTestSchedule);
        });
    }

    @Test
    public void shouldDeleteDoctorWithoutAffectingDepartment() {
        //then
        Department department = createDepartment();
        MedicalTestSchedule medicalTestSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));
        //when
        MedicalTestSchedule save = medicalTestScheduleRepository.save(medicalTestSchedule);
        medicalTestScheduleRepository.flush();
        medicalTestScheduleRepository.deleteById(save.getId());
        //then
        Optional<MedicalTestSchedule> testScheduleOptional = medicalTestScheduleRepository.findById(save.getId());
        assertTrue(testScheduleOptional.isEmpty());
        Optional<Department> departmentOptional = departmentRepository.findById(department.getId());
        assertTrue(departmentOptional.isPresent());
        Department departmentAfterDelete = departmentOptional.get();
        assertFalse(departmentAfterDelete.getMedicalTestSchedules().contains(save));
    }

    @Test
    public void shouldNotCreateTestScheduleWhenEndDateTimeIsNull() {
        //given
        Department department = createDepartment();
        MedicalTestSchedule medicalTestSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, null,
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            medicalTestScheduleRepository.saveAndFlush(medicalTestSchedule);
        });
    }

    private void assertSchedules(MedicalTestSchedule s1, MedicalTestSchedule s2) {
        assertEquals(s1.getStartDateTime(), s2.getStartDateTime());
        assertEquals(s1.getEndDateTime(), s2.getEndDateTime());
        assertEquals(s1.getType(), s2.getType());
        assertEquals(s1.getDepartment(), s2.getDepartment());
    }

    private MedicalTestSchedule createTestSchedule(Department department, TestType testType, Timestamp startDateTime, Timestamp endDateTime) {
        MedicalTestSchedule medicalTestSchedule = new MedicalTestSchedule();
        medicalTestSchedule.setDepartment(department);
        medicalTestSchedule.setType(testType);
        medicalTestSchedule.setStartDateTime(startDateTime);
        medicalTestSchedule.setEndDateTime(endDateTime);
        return medicalTestSchedule;
    }

    private Department createDepartment() {
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
