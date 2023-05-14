package pl.akh.domainservicesvc.domain.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.utils.auth.oauth.OAuthDataExtractorFacade;
import pl.akh.domainservicesvc.domain.utils.roles.HasAnyAdministrativeRole;
import pl.akh.domainservicesvc.domain.utils.roles.HasRolePatient;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleReceptionist;
import pl.akh.model.rq.PatientDataRQ;
import pl.akh.model.rs.PatientRS;
import pl.akh.services.PatientService;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Slf4j
@Validated
public class PatientController {

    private static final Integer PAGE_SIZE = 10;
    private final PatientService patientService;
    private final OAuthDataExtractorFacade oAuthDataExtractorFacade;

    public PatientController(PatientService patientService, OAuthDataExtractorFacade oAuthDataExtractorFacade) {
        this.patientService = patientService;
        this.oAuthDataExtractorFacade = oAuthDataExtractorFacade;
    }

    @HasAnyAdministrativeRole
    @GetMapping(path = "/{uuid}")
    public ResponseEntity<PatientRS> getPatientByUUID(@PathVariable @NotNull UUID uuid) {
        try {
            Optional<PatientRS> patient = patientService.getPatientById(uuid);
            if (patient.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return patient.map(ResponseEntity::ok).get();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @HasRolePatient
    @GetMapping
    public ResponseEntity<PatientRS> getPatientData() {
        try {
            Optional<PatientRS> patient = patientService.getPatientById(oAuthDataExtractorFacade.getId());
            if (patient.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return patient.map(ResponseEntity::ok).get();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @HasRolePatient
    @PutMapping
    public ResponseEntity<PatientRS> updatePatientData(@RequestBody @Valid PatientDataRQ patientData) {
        try {
            PatientRS patientRS = patientService.updatePatientData(oAuthDataExtractorFacade.getId(), patientData);
            return ResponseEntity.ok(patientRS);
        } catch (Exception e) {
            log.error("", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //should we use this method? may use 404 code on fetching patient data
    @HasRolePatient
    @GetMapping(path = "is-updated/{uuid}")
    public ResponseEntity<Boolean> hasPatientDataUpdated(@PathVariable @NotNull UUID uuid) {
        try {
            boolean updated = patientService.hasPatientDataUpdated(uuid);
            if (updated) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @HasRoleReceptionist
    @GetMapping("/all")
    public ResponseEntity<Collection<PatientRS>> getAllPatients(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", required = false) Integer pageSize,
                                                                @RequestParam(name = "firstName", required = false) String firstName,
                                                                @RequestParam(name = "lastName", required = false) String lastName,
                                                                @RequestParam(name = "phoneNumber", required = false) String phoneNumber) {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null || pageSize < 10) {
            pageSize = Integer.MAX_VALUE;
        }
        Collection<PatientRS> patients = patientService.getPatientsByCriteria(pageNumber, pageSize, firstName, lastName, phoneNumber);
        return ResponseEntity.ok(patients);
    }
}
