package pl.akh.services;

import pl.akh.model.rq.PatientDataRQ;
import pl.akh.model.rs.PatientRS;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {
    Optional<PatientRS> getPatientById(UUID uuid);

    PatientRS updatePatientData(UUID patientUUID, PatientDataRQ patientData);

    boolean hasPatientDataUpdated(UUID uuid);

    Collection<PatientRS> getPatientsByCriteria(Integer page, Integer pageSize, String firstName, String lastName, String phoneNumber);
}
