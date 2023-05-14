package pl.akh.domainservicesvc.domain.services.api;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.mappers.AddressMapper;
import pl.akh.domainservicesvc.domain.mappers.PatientMapper;
import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.domainservicesvc.domain.model.entities.enums.Gender;
import pl.akh.domainservicesvc.domain.model.entities.Patient;
import pl.akh.domainservicesvc.domain.repository.PatientRepository;
import pl.akh.model.common.Specialization;
import pl.akh.model.rq.PatientDataRQ;
import pl.akh.model.rs.PatientRS;
import pl.akh.services.PatientService;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<PatientRS> getPatientById(UUID uuid) {
        return patientRepository.findById(uuid).map(PatientMapper::mapToDto);
    }

    @Override
    public PatientRS updatePatientData(UUID patientUUID, PatientDataRQ patientData) {
        Patient patient = patientRepository.findById(patientUUID)
                .orElse(new Patient(patientUUID));

        patient.setFirstName(patientData.getFirstName());
        patient.setLastName(patientData.getLastName());
        patient.setGender(Gender.valueOf(patientData.getGender()));
        patient.setPhoneNumber(patientData.getPhoneNumber());
        patient.setPesel(patientData.getPesel());
        patient.setAddress(AddressMapper.mapToEntity(patientData.getAddressRQ()));
        Patient updated = patientRepository.save(patient);
        return PatientMapper.mapToDto(updated);
    }

    @Override
    public boolean hasPatientDataUpdated(UUID uuid) {
        Optional<Patient> patient = patientRepository.findById(uuid);
        return patient.isPresent();
    }

    @Override
    public Collection<PatientRS> getPatientsByCriteria(Integer pageNumber, Integer pageSize, String firstName, String lastName, String phoneNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return patientRepository.findAll(createPatientCriteria(firstName, lastName, phoneNumber), pageable)
                .stream()
                .map(PatientMapper::mapToDtoWithoutSensitiveData)
                .toList();
    }

    private Specification<Patient> createPatientCriteria(String firstName, String lastName, String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            ofNullable(phoneNumber).ifPresent((pNumber) -> predicates.add(criteriaBuilder.like(root.get("phoneNumber"), pNumber + "%")));
            ofNullable(firstName).ifPresent((fName) -> predicates.add(criteriaBuilder.like(root.get("firstName"), fName + "%")));
            ofNullable(lastName).ifPresent((lName) -> predicates.add(criteriaBuilder.like(root.get("lastName"), lName + "%")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
