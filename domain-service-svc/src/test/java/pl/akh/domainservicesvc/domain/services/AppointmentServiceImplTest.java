package pl.akh.domainservicesvc.domain.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.exceptions.AppointmentConflictException;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PatientNotFoundException;
import pl.akh.domainservicesvc.domain.model.entities.*;
import pl.akh.domainservicesvc.domain.model.entities.enums.Gender;
import pl.akh.domainservicesvc.domain.model.entities.enums.Specialization;
import pl.akh.domainservicesvc.domain.repository.AppointmentRepository;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.repository.DoctorRepository;
import pl.akh.domainservicesvc.domain.repository.PatientRepository;
import pl.akh.domainservicesvc.domain.services.api.AppointmentServiceImpl;
import pl.akh.domainservicesvc.domain.services.api.ScheduleServiceImpl;
import pl.akh.model.rq.AppointmentRQ;
import pl.akh.model.rq.ScheduleRQ;
import pl.akh.model.rs.AppointmentRS;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertThrows;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest extends DomainServiceIntegrationTest {

    @Autowired
    AppointmentServiceImpl appointmentService;
    @Autowired
    ScheduleServiceImpl scheduleService;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    AppointmentRepository appointmentRepository;

    @Test
    void shouldCreateAppointment() throws Exception {
        //given
        Doctor doctor = createDoctor(UUID.randomUUID());
        Patient patient = createPatient(UUID.randomUUID());
        AppointmentRQ appointmentRQ = AppointmentRQ.builder()
                .doctorUUID(doctor.getId())
                .patientUUID(patient.getId())
                .appointmentDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 15))).build();

        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(9, 0), fromHHMM(12, 0)),
                create(fromHHMM(13, 15), fromHHMM(14, 45)));

        scheduleService.insertSchedules(doctor.getId(), schedules);

        //when
        AppointmentRS appointment = appointmentService.createAppointment(appointmentRQ);
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointment.getId());

        //then
        Assertions.assertTrue(optionalAppointment.isPresent());
    }

    @Test
    void shouldNotCreateAppointmentWhenDoctorNotFound() {
        //given
        UUID uuid = UUID.randomUUID();
        Patient patient = createPatient(UUID.randomUUID());
        AppointmentRQ appointmentRQ = AppointmentRQ.builder()
                .doctorUUID(uuid)
                .patientUUID(patient.getId())
                .appointmentDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 15))).build();

        //when
        //then
        assertThrows(DoctorNotFoundException.class, () -> {
            appointmentService.createAppointment(appointmentRQ);
        });
    }

    @Test
    void shouldNotCreateAppointmentWhenPatientNotFound() {
        //given
        UUID uuid = UUID.randomUUID();
        Doctor doctor = createDoctor(UUID.randomUUID());
        AppointmentRQ appointmentRQ = AppointmentRQ.builder()
                .doctorUUID(doctor.getId())
                .patientUUID(uuid)
                .appointmentDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 15))).build();

        //when
        //then
        assertThrows(PatientNotFoundException.class, () -> {
            appointmentService.createAppointment(appointmentRQ);
        });
    }

    @Test
    void shouldNotCreateAppointmentWhenIsAppointmentConflict() throws Exception {
        //given
        Doctor doctor = createDoctor(UUID.randomUUID());
        Patient patient = createPatient(UUID.randomUUID());
        AppointmentRQ appointmentRQ = AppointmentRQ.builder()
                .doctorUUID(doctor.getId())
                .patientUUID(patient.getId())
                .appointmentDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30))).build();

        AppointmentRQ appointmentRQ2 = AppointmentRQ.builder()
                .doctorUUID(doctor.getId())
                .patientUUID(patient.getId())
                .appointmentDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30))).build();

        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(9, 0), fromHHMM(12, 0)),
                create(fromHHMM(13, 15), fromHHMM(14, 45)));

        scheduleService.insertSchedules(doctor.getId(), schedules);

        //when
        appointmentService.createAppointment(appointmentRQ);

        //then
        assertThrows(AppointmentConflictException.class, () -> {
            appointmentService.createAppointment(appointmentRQ2);
        });
    }

    @Test
    void shouldNotCreateAppointmentWhenTimeIsInvalid() {
        //given
        Doctor doctor = createDoctor(UUID.randomUUID());
        Patient patient = createPatient(UUID.randomUUID());
        AppointmentRQ appointmentRQ = AppointmentRQ.builder()
                .doctorUUID(doctor.getId())
                .patientUUID(patient.getId())
                .appointmentDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 10))).build();


        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(9, 0), fromHHMM(12, 0)),
                create(fromHHMM(13, 15), fromHHMM(14, 45)));

        scheduleService.insertSchedules(doctor.getId(), schedules);

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.createAppointment(appointmentRQ);
        });
    }

    @Test
    void shouldNotCreateAppointmentWhenAppointmentDateIsWrong() {
        //given
        Doctor doctor = createDoctor(UUID.randomUUID());
        Patient patient = createPatient(UUID.randomUUID());
        AppointmentRQ appointmentRQ = AppointmentRQ.builder()
                .doctorUUID(doctor.getId())
                .patientUUID(patient.getId())
                .appointmentDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0))).build();


        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(9, 0), fromHHMM(12, 0)),
                create(fromHHMM(13, 15), fromHHMM(14, 45)));

        scheduleService.insertSchedules(doctor.getId(), schedules);

        //when
        //then
        assertThrows(AppointmentConflictException.class, () -> {
            appointmentService.createAppointment(appointmentRQ);
        });
    }

    private Doctor createDoctor(UUID uuid) {
        Doctor doctor = new Doctor();
        doctor.setId(uuid);
        doctor.setDepartment(prepareDepartment());
        doctor.setSpecialization(Specialization.ANESTHESIA);
        doctor.setFirstName("James");
        doctor.setLastName("McGill");
        return doctorRepository.saveAndFlush(doctor);
    }

    private Patient createPatient(UUID uuid) {
        Patient patient = new Patient();
        patient.setId(uuid);
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

    private LocalDateTime fromHHMM(int h, int m) {
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), h, m);
    }

    private ScheduleRQ create(LocalDateTime start, LocalDateTime end) {
        return ScheduleRQ.builder()
                .startDateTime(start)
                .endDateTime(end)
                .build();
    }
}