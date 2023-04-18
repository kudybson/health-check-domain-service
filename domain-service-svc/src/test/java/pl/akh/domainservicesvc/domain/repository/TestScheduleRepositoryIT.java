package pl.akh.domainservicesvc.domain.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.Address;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.TestSchedule;
import pl.akh.domainservicesvc.domain.model.entities.TestType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;
import static pl.akh.domainservicesvc.domain.model.entities.TestType.BLOOD_ALLERGY_TESTS;

public class TestScheduleRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private TestScheduleRepository testScheduleRepository;

    @Test
    public void shouldCreateTestSchedule() {
        //given
        Department department = createDepartment();
        TestSchedule testSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        testScheduleRepository.saveAndFlush(testSchedule);

        //then
        Assertions.assertEquals(1, testScheduleRepository.findAll().size());
    }

    @Test
    public void shouldDeleteTestSchedule() {
        //given
        Department department = createDepartment();
        TestSchedule testSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        TestSchedule save = testScheduleRepository.saveAndFlush(testSchedule);
        testScheduleRepository.delete(save);

        //then
        Assertions.assertEquals(0, testScheduleRepository.findAll().size());
    }

    @Test
    public void shouldNotCreateTestScheduleWhenDepartmentIsNull() {
        //given
        TestSchedule testSchedule = createTestSchedule(null, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            testScheduleRepository.saveAndFlush(testSchedule);
        });
    }

    @Test
    public void shouldNotCreateTestScheduleWhenTestTypeIsNull() {
        //given
        Department department = createDepartment();
        TestSchedule testSchedule = createTestSchedule(department, null, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            testScheduleRepository.saveAndFlush(testSchedule);
        });
    }

    @Test
    public void shouldNotCreateTestScheduleWhenStartDateTimeIsNull() {
        //given
        Department department = createDepartment();
        TestSchedule testSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                null);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            testScheduleRepository.saveAndFlush(testSchedule);
        });
    }

    @Test
    public void shouldDeleteDoctorWithoutAffectingDepartment() {
        //then
        Department department = createDepartment();
        TestSchedule testSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 8, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));
        //when
        TestSchedule save = testScheduleRepository.save(testSchedule);
        testScheduleRepository.flush();
        testScheduleRepository.deleteById(save.getId());
        //then
        Optional<TestSchedule> testScheduleOptional = testScheduleRepository.findById(save.getId());
        assertTrue(testScheduleOptional.isEmpty());
        Optional<Department> departmentOptional = departmentRepository.findById(department.getId());
        assertTrue(departmentOptional.isPresent());
        Department departmentAfterDelete = departmentOptional.get();
        assertFalse(departmentAfterDelete.getTestSchedules().contains(save));
    }

    @Test
    public void shouldNotCreateTestScheduleWhenEndDateTimeIsNull() {
        //given
        Department department = createDepartment();
        TestSchedule testSchedule = createTestSchedule(department, BLOOD_ALLERGY_TESTS, null,
                Timestamp.valueOf(LocalDateTime.of(2023, 4, 16, 16, 0)));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            testScheduleRepository.saveAndFlush(testSchedule);
        });
    }

    private TestSchedule createTestSchedule(Department department, TestType testType, Timestamp startDateTime, Timestamp endDateTime) {
        TestSchedule testSchedule = new TestSchedule();
        testSchedule.setDepartment(department);
        testSchedule.setType(testType);
        testSchedule.setStartDateTime(startDateTime);
        testSchedule.setEndDateTime(endDateTime);
        return testSchedule;
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
