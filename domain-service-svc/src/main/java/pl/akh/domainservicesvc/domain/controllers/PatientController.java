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
import pl.akh.model.rq.PatientDataRQ;
import pl.akh.model.rs.PatientRS;
import pl.akh.services.PatientService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Slf4j
@Validated
public class PatientController {

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
}
