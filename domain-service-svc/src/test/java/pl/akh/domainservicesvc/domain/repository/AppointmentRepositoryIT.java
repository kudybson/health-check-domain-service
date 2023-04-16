package pl.akh.domainservicesvc.domain.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
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
        appointmentRepository.saveAndFlush(appointment);

        //then
        List<Appointment> appointments = appointmentRepository.findAll();
        assertEquals(1L, appointments.size());
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
        Appointment appointment = createAppointment(null, prepareDepartment(),
                Timestamp.valueOf(LocalDateTime.now()), createDoctor(), createPatient());

        //when
        appointment = appointmentRepository.saveAndFlush(appointment);
        appointmentRepository.delete(appointment);

        //then
        List<Appointment> appointments = appointmentRepository.findAll();
        assertEquals(0L, appointments.size());
        List<Doctor> doctors = doctorRepository.findAll();
        assertEquals(1L, doctors.size());
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
        List<Appointment> appointments = appointmentRepository.findAll();
        assertEquals(0L, appointments.size());
        List<Patient> patients = patientRepository.findAll();
        assertEquals(1L, patients.size());
    }

    private Appointment createAppointment(String comments, Department department, Timestamp appointmentDate, Doctor doctor, Patient patient) {
        Appointment appointment = new Appointment();
        appointment.setComments(comments);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setDepartment(department);
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
