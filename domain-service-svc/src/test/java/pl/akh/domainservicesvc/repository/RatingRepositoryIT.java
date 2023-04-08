package pl.akh.domainservicesvc.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.model.entities.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RatingRepositoryIT extends DomainServiceIntegrationTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private UUID patientUUID = UUID.randomUUID();
    private UUID doctorUUID = UUID.randomUUID();

    @Test
    public void shouldCreateRatingWithDescription() {
        //given
        Rating rating = createRating(1L, "description", createDoctor(), createPatient());

        //when
        rating = ratingRepository.saveAndFlush(rating);

        //then
        List<Rating> ratings = ratingRepository.findAll();
        assertEquals(1L, ratings.size());
        Optional<Rating> savedRatingOptional = ratings.stream().findFirst();
        assertTrue(savedRatingOptional.isPresent());
        Rating savedRating = savedRatingOptional.get();
        assertEquals(savedRating, rating);
    }

    @Test
    public void shouldCreateRatingWithoutDescription() {
        //given
        Rating rating = createRating(1L, null, createDoctor(), createPatient());

        //when
        rating = ratingRepository.saveAndFlush(rating);

        //then
        List<Rating> ratings = ratingRepository.findAll();
        assertEquals(1L, ratings.size());
        Optional<Rating> savedRatingOptional = ratings.stream().findFirst();
        assertTrue(savedRatingOptional.isPresent());
        Rating savedRating = savedRatingOptional.get();
        assertEquals(savedRating, rating);
    }

    @Test
    public void shouldNotCreateRatingBecauseDoctorIsNull() {
        //given
        Rating rating = createRating(1L, "description", null, createPatient());

        //when
        assertThrows(ConstraintViolationException.class, () -> {
            ratingRepository.saveAndFlush(rating);
        });
    }

    @Test
    public void shouldNotCreateRatingBecausePatientIsNull() {
        //given
        Rating rating = createRating(1L, "description", createDoctor(), null);

        //when
        assertThrows(ConstraintViolationException.class, () -> {
            ratingRepository.saveAndFlush(rating);
        });
    }

    @Test
    public void shouldNotCreateRatingBecauseIsLesserThenMinimum() {
        //given
        Rating rating = createRating(-1L, "description", createDoctor(), createPatient());

        //when
        assertThrows(ConstraintViolationException.class, () -> {
            ratingRepository.saveAndFlush(rating);
        });
    }

    @Test
    public void shouldNotCreateRatingBecauseIsGraterThenMaximum() {
        //given
        Rating rating = createRating(11L, "description", createDoctor(), createPatient());

        //when
        assertThrows(ConstraintViolationException.class, () -> {
            ratingRepository.saveAndFlush(rating);
        });
    }

    private Rating createRating(Long grade, String description, Doctor doctor, Patient patient) {
        Rating rating = new Rating();
        rating.setGrade(grade);
        rating.setDescription(description);
        rating.setPatient(patient);
        rating.setDoctor(doctor);
        return rating;
    }

    private Doctor createDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(doctorUUID);
        doctor.setDepartment(prepareDepartment());
        doctor.setSpecialization(Specialization.ANESTHESIA);
        doctor.setFirstName("James");
        doctor.setSecondName("McGill");
        return doctorRepository.saveAndFlush(doctor);
    }

    private Patient createPatient() {
        Patient patient = new Patient();
        patient.setId(patientUUID);
        patient.setFirstName("John");
        patient.setSecondName("Wick");
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
