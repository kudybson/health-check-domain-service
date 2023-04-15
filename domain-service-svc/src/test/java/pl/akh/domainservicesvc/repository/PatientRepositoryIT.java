package pl.akh.domainservicesvc.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import pl.akh.domainservicesvc.DomainServiceIntegrationTest;
import pl.akh.domainservicesvc.model.entities.Address;
import pl.akh.domainservicesvc.model.entities.Gender;
import pl.akh.domainservicesvc.model.entities.Patient;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PatientRepositoryIT extends DomainServiceIntegrationTest {
    @Autowired
    private PatientRepository patientRepository;

    private final UUID uuid = UUID.randomUUID();
    private final String firstName = "John";
    private final String lastName = "Wick";
    private final String pesel = "00210147133";
    private final String phone = "797634917";
    private final Gender gender = Gender.MALE;
    private final Address address = prepareAddress();

    @Test
    public void shouldCreatePatientWithAddress() {
        //given
        Patient patient = createPatient(uuid, pesel, phone, firstName, lastName, gender, address);

        //when
        patientRepository.saveAndFlush(patient);

        //then
        Optional<Patient> savedPatientOptional = patientRepository.findById(uuid);
        assertTrue(savedPatientOptional.isPresent());
        Patient savedPatient = savedPatientOptional.get();
        assertEquals(uuid, savedPatient.getId());
        assertEquals(firstName, savedPatient.getFirstName());
        assertEquals(lastName, savedPatient.getSecondName());
        assertNotNull(savedPatient.getAddress());
    }
    @Test
    public void shouldNotCreatePatientBecauseIdIsNull() {
        //given
        Patient patient = createPatient(null, pesel, phone, "", lastName, gender, address);
        //when
        //then
        assertThrows(JpaSystemException.class, () -> {
            patientRepository.saveAndFlush(patient);
        });
    }
    @Test
    public void shouldNotCreatePatientBecausePeselIsEmpty() {
        //given
        Patient patient = createPatient(uuid, "", phone, firstName, lastName, gender, address);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            patientRepository.saveAndFlush(patient);
        });
    }

    @Test
    public void shouldNotCreatePatientBecausePhoneNumberNameIsEmpty() {
        //given
        Patient patient = createPatient(uuid, pesel, "", firstName, lastName, gender, address);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            patientRepository.saveAndFlush(patient);
        });
    }

    @Test
    public void shouldNotCreatePatientBecauseFirstNameIsEmpty() {
        //given
        Patient patient = createPatient(uuid, pesel, phone, "", lastName, gender, address);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            patientRepository.saveAndFlush(patient);
        });
    }

    @Test
    public void shouldNotCreatePatientBecauseLastNameIsEmpty() {
        //given
        Patient patient = createPatient(uuid, pesel, phone, firstName, "", gender, address);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            patientRepository.saveAndFlush(patient);
        });
    }

    @Test
    public void shouldNotCreatePatientBecauseGenderIsNull() {
        //given
        Patient patient = createPatient(uuid, pesel, phone, firstName, lastName, null, address);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            patientRepository.saveAndFlush(patient);
        });
    }

    @Test
    public void shouldNotCreatePatientBecauseAddressIsNull() {
        //given
        Patient patient = createPatient(uuid, pesel, phone, firstName, lastName, gender, null);
        //when
        //then
        assertThrows(ConstraintViolationException.class, () -> {
            patientRepository.saveAndFlush(patient);
        });
    }

    private Patient createPatient(UUID id, String pesel, String phone, String firstName, String lastName, Gender gender, Address address) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setFirstName(firstName);
        patient.setSecondName(lastName);
        patient.setGender(gender);
        patient.setPhoneNumber(phone);
        patient.setPesel(pesel);
        patient.setAddress(address);
        return patient;
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
