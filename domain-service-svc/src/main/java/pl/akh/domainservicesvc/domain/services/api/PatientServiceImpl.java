package pl.akh.domainservicesvc.domain.services.api;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.mappers.AddressMapper;
import pl.akh.domainservicesvc.domain.mappers.PatientMapper;
import pl.akh.domainservicesvc.domain.model.entities.enums.Gender;
import pl.akh.domainservicesvc.domain.model.entities.Patient;
import pl.akh.domainservicesvc.domain.repository.PatientRepository;
import pl.akh.model.rq.PatientDataRQ;
import pl.akh.model.rs.PatientRS;
import pl.akh.services.PatientService;

import java.util.Optional;
import java.util.UUID;

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
}
