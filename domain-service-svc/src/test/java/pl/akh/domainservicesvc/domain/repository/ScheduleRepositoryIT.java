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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScheduleRepositoryIT extends DomainServiceIntegrationTest {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    private final UUID doctorUUID = UUID.randomUUID();
    private final Timestamp NOW = Timestamp.from(Instant.now());

    @Test
    public void shouldAddNewSchedule() {
        //given
        Doctor doctor = prepareDoctor();
        Schedule schedule = createSchedule(doctor, NOW, NOW);

        //when
        Schedule savedSchedule = scheduleRepository.saveAndFlush(schedule);
        //then
        assertTrue(scheduleRepository.findById(savedSchedule.getId()).isPresent());
    }

    @Test
    public void shouldNotAddNewScheduleWhenStartDateIsNull() {
        //given
        Doctor doctor = prepareDoctor();
        Schedule schedule = createSchedule(doctor, null, NOW);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            scheduleRepository.saveAndFlush(schedule);
        });
    }

    @Test
    public void shouldNotAddNewScheduleWhenEndDateIsNull() {
        //given
        Doctor doctor = prepareDoctor();
        Schedule schedule = createSchedule(doctor, NOW, null);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            scheduleRepository.saveAndFlush(schedule);
        });
    }

    @Test
    public void shouldNotAddNewScheduleWhenDoctorIsNull() {
        //given
        Schedule schedule = createSchedule(null, NOW, NOW);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            scheduleRepository.saveAndFlush(schedule);
        });
    }

    private Schedule createSchedule(Doctor doctor, Timestamp startTime, Timestamp endTime) {
        Schedule schedule = new Schedule();
        schedule.setDoctor(doctor);
        schedule.setStartDateTime(startTime);
        schedule.setEndDateTime(endTime);
        return schedule;
    }

    private Doctor prepareDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(doctorUUID);
        doctor.setSpecialization(Specialization.ANESTHESIA);
        doctor.setLastName("Nazwisko");
        doctor.setFirstName("Imie");
        doctor.setDepartment(prepareDepartment());
        return doctorRepository.saveAndFlush(doctor);
    }

    private Department prepareDepartment() {
        Department department = new Department();
        department.setName("ScheduleRepositoryIT department");
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
