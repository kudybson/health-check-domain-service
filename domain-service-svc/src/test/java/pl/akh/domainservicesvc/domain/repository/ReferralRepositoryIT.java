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
import pl.akh.domainservicesvc.domain.model.entities.enums.TestType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.*;
import static pl.akh.domainservicesvc.domain.model.entities.enums.TestType.HARMONY_TEST;

public class ReferralRepositoryIT extends DomainServiceIntegrationTest {

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
    private ReferralRepository referralRepository;

    @Test
    public void shouldCreateReferral() {
        //given
        Treatment treatment = createTreatment();
        Referral referral = createReferral(HARMONY_TEST, treatment, Timestamp.from(Instant.now()));

        //when
        referralRepository.saveAndFlush(referral);

        //then
        Assertions.assertEquals(1, referralRepository.findAll().size());
    }

    @Test
    public void shouldNotCreateReferralWhenTreatmentIsNull() {
        //given
        Referral referral = createReferral(HARMONY_TEST, null, Timestamp.from(Instant.now()));
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            referralRepository.saveAndFlush(referral);
        });
    }

    @Test
    public void shouldNotCreateReferralWhenTestTypeIsNull() {
        //given
        Treatment treatment = createTreatment();
        Referral referral = createReferral(null, treatment, Timestamp.from(Instant.now()));

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            referralRepository.saveAndFlush(referral);
        });
    }

    @Test
    public void shouldNotCreateReferralWhenExpirationDateIsNull() {
        //given
        Treatment treatment = createTreatment();
        Referral referral = createReferral(HARMONY_TEST, treatment, null);

        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            referralRepository.saveAndFlush(referral);
        });
    }

    private Referral createReferral(TestType testType, Treatment treatment, Timestamp expirationDate) {
        Referral referral = new Referral();
        referral.setTreatment(treatment);
        referral.setTestType(testType);
        referral.setExpirationDate(expirationDate);
        return referral;
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
