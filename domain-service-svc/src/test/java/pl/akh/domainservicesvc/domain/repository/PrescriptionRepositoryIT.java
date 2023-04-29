package pl.akh.domainservicesvc.domain.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.domain.model.entities.*;
import pl.akh.domainservicesvc.domain.model.entities.enums.Gender;
import pl.akh.domainservicesvc.domain.model.entities.enums.Specialization;
import pl.akh.domainservicesvc.domain.model.entities.enums.Status;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertThrows;

public class PrescriptionRepositoryIT extends DomainServiceIntegrationTest {

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
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Test
    public void shouldCreatePrescription() {
        //given
        Treatment treatment = createTreatment();
        Prescription prescription = createPrescription(treatment, "123456", "Mucosolvan", Timestamp.from(Instant.now()));

        //when
        prescriptionRepository.saveAndFlush(prescription);

        //then
        Assertions.assertEquals(1, prescriptionRepository.findAll().size());
    }

    @Test
    public void shouldNotCreatePrescriptionWhenTreatmentIsNull() {
        //given
        Prescription prescription = createPrescription(null, "123456", "Mucosolvan", Timestamp.from(Instant.now()));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            prescriptionRepository.saveAndFlush(prescription);
        });
    }

    @Test
    public void shouldNotCreatePrescriptionWhenCodeIsEmpty() {
        //given
        Treatment treatment = createTreatment();
        Prescription prescription = createPrescription(treatment, "", "Mucosolvan", Timestamp.from(Instant.now()));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            prescriptionRepository.saveAndFlush(prescription);
        });
    }

    @Test
    public void shouldNotCreatePrescriptionWhenDescriptionIsEmpty() {
        //given
        Treatment treatment = createTreatment();
        Prescription prescription = createPrescription(treatment, "123456", "", Timestamp.from(Instant.now()));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            prescriptionRepository.saveAndFlush(prescription);
        });
    }

    @Test
    public void shouldNotCreatePrescriptionWhenExpirationDateIsNull() {
        //given
        Treatment treatment = createTreatment();
        Prescription prescription = createPrescription(treatment, "123456", "Mucosolvan", null);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            prescriptionRepository.saveAndFlush(prescription);
        });
    }

    private Prescription createPrescription(Treatment treatment, String code, String description, Timestamp expirationDate) {
        Prescription prescription = new Prescription();
        prescription.setTreatment(treatment);
        prescription.setCode(code);
        prescription.setDescription(description);
        prescription.setExpirationDate(expirationDate);
        return prescription;
    }

    private Treatment createTreatment() {
        Treatment treatment = new Treatment();
        treatment.setAppointment(createAppointment());
        treatment.setDiagnosis("zwichnięta kostka");
        treatment.setReferral(null);
        treatment.setRecommendation("unikanie aktywności fizycznej");
        treatment.setPrescription(null);
        return treatmentRepository.saveAndFlush(treatment);
    }

    private Appointment createAppointment() {
        Doctor doctor = createDoctor();
        Appointment appointment = new Appointment();
        appointment.setComments("comments");
        appointment.setAppointmentDate(Timestamp.from(Instant.now()));
        appointment.setDepartment(doctor.getDepartment());
        appointment.setStatus(Status.SCHEDULED);
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
