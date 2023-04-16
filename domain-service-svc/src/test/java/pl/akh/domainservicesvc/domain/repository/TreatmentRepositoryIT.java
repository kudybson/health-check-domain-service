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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TreatmentRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private TreatmentRepository treatmentRepository;


    @Test
    public void shouldCreateTreatment() {
        //given
        Appointment appointment = createAppointment();
        Treatment treatment = createTreatment(appointment, "zwichnięta kostka", "unikanie aktywności fizycznej", null, null);
        //when
        Treatment savedTreatment = treatmentRepository.saveAndFlush(treatment);
        //then

        Assertions.assertEquals(1, treatmentRepository.findAll().size());
    }

    @Test
    public void shouldNotCreateTreatmentWhenAppointmentIsNull() {
        //given
        Appointment appointment = createAppointment();
        Treatment treatment = createTreatment(null, "zwichnięta kostka", "unikanie aktywności fizycznej", null, null);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            Treatment savedTreatment = treatmentRepository.saveAndFlush(treatment);
        });
    }

    @Test
    public void shouldNotCreateTreatmentWhenDiagnosisIsNull() {
        //given
        Appointment appointment = createAppointment();
        Treatment treatment = createTreatment(appointment, null, "unikanie aktywności fizycznej", null, null);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            Treatment savedTreatment = treatmentRepository.saveAndFlush(treatment);
        });
    }

    @Test
    public void shouldNotCreateTreatmentWhenRecommendationIsEmpty() {
        //given
        Appointment appointment = createAppointment();
        Treatment treatment = createTreatment(appointment, "zwichnięta kostka", "", null, null);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            Treatment savedTreatment = treatmentRepository.saveAndFlush(treatment);
        });
    }

    private Treatment createTreatment(Appointment appointment, String diagnosis, String recommendation, Referral referral, Prescription prescription) {
        Treatment treatment = new Treatment();
        treatment.setAppointment(appointment);
        treatment.setDiagnosis(diagnosis);
        treatment.setReferral(referral);
        treatment.setRecommendation(recommendation);
        treatment.setPrescription(prescription);
        return treatment;
    }

    private Appointment createAppointment() {
        Doctor doctor = createDoctor();
        Appointment appointment = new Appointment();
        appointment.setComments("comments");
        appointment.setAppointmentDate(Timestamp.from(Instant.now()));
        appointment.setDepartment(doctor.getDepartment());
        appointment.setPatient(createPatient());
        appointment.setDoctor(doctor);
        return appointmentRepository.saveAndFlush(appointment);
    }

    private Doctor createDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        doctor.setDepartment(prepareDepartment());
        doctor.setSpecialization(Specialization.ANESTHESIA);
        doctor.setFirstName("James");
        doctor.setLastName("McGill");
        return doctorRepository.saveAndFlush(doctor);
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
