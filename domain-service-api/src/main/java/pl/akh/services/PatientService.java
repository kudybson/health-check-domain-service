package pl.akh.services;

import pl.akh.model.rq.PatientDataRQ;
import pl.akh.model.rs.PatientRS;

import java.util.UUID;

public interface PatientService {
    PatientRS getPatientById(UUID uuid);

    PatientRS updatePatientData(UUID patientUUID, PatientDataRQ patientData);

    boolean hasPatientDataUpdated(UUID uuid);
}
