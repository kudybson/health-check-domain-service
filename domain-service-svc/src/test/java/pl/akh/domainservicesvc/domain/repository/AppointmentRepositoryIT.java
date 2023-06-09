package pl.akh.domainservicesvc.domain.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.*;
import pl.akh.domainservicesvc.domain.model.entities.enums.Gender;
import pl.akh.domainservicesvc.domain.model.entities.enums.Specialization;
import pl.akh.domainservicesvc.domain.model.entities.enums.Status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AppointmentRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    private UUID patientUUID = UUID.randomUUID();
    private UUID doctorUUID = UUID.randomUUID();

    @Test
    public void shouldCreateAppointment() {
        //given
        Appointment appointment = createAppointment(null, prepareDepartment(),
                Timestamp.valueOf(LocalDateTime.now()), createDoctor(), createPatient());

        //when
        Appointment saved = appointmentRepository.saveAndFlush(appointment);

        //then
        assertTrue(appointmentRepository.findById(saved.getId()).isPresent());
    }

    @Test
    public void shouldNotCreateAppointmentBecauseDoctorIsNull() {
        //given
        Appointment appointment = createAppointment("wizyta kontrolna", prepareDepartment(), Timestamp.valueOf(LocalDateTime.now()), null, createPatient());

        //when
        assertThrows(ConstraintViolationException.class, () -> {
            appointmentRepository.saveAndFlush(appointment);
        });
    }


    @Test
    public void shouldNotCreateAppointmentBecauseDepartmentIsNull() {
        //given
        Appointment appointment = createAppointment("wizyta kontrolna", null, Timestamp.valueOf(LocalDateTime.now()), createDoctor(), createPatient());

        //when
        assertThrows(ConstraintViolationException.class, () -> {
            appointmentRepository.saveAndFlush(appointment);
        });
    }

    @Test
    public void shouldDeleteAppointmentWithoutDeleteDoctor() {
        //given
        Doctor doctor = createDoctor();
        Appointment appointment = createAppointment(null, prepareDepartment(),
                Timestamp.valueOf(LocalDateTime.now()), doctor, createPatient());

        //when

        appointment = appointmentRepository.saveAndFlush(appointment);
        Long id = appointment.getId();
        appointmentRepository.delete(appointment);

        //then
        assertTrue(appointmentRepository.findById(id).isEmpty());
        assertNotNull(doctorRepository.findById(doctorUUID));
    }

    @Test
    public void shouldDeleteAppointmentWithoutDeletePatient() {
        //given
        Appointment appointment = createAppointment(null, prepareDepartment(),
                Timestamp.valueOf(LocalDateTime.now()), createDoctor(), createPatient());

        //when
        appointment = appointmentRepository.saveAndFlush(appointment);
        appointmentRepository.delete(appointment);

        //then
        assertTrue(appointmentRepository.findById(appointment.getId()).isEmpty());
        assertNotNull(patientRepository.findById(patientUUID));
    }

    private Appointment createAppointment(String comments, Department department, Timestamp appointmentDate, Doctor doctor, Patient patient) {
        Appointment appointment = new Appointment();
        appointment.setComments(comments);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setDepartment(department);
        appointment.setStatus(Status.SCHEDULED);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        return appointment;
    }

    private Doctor createDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(doctorUUID);
        doctor.setDepartment(prepareDepartment());
        doctor.setSpecialization(Specialization.ANESTHESIA);
        doctor.setFirstName("James");
        doctor.setLastName("McGill");
        return doctorRepository.saveAndFlush(doctor);
    }

    private Patient createPatient() {
        Patient patient = new Patient();
        patient.setId(patientUUID);
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
