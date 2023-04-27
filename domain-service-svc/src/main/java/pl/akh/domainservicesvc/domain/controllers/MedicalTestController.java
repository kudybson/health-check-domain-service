package pl.akh.domainservicesvc.domain.controllers;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import jdk.jfr.Description;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.OverwritingTimeException;
import pl.akh.domainservicesvc.domain.services.AccessGuard;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleReceptionist;
import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestRQ;
import pl.akh.model.rs.MedicalTestRS;
import pl.akh.services.MedicalTestService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static pl.akh.domainservicesvc.domain.utils.DateUtils.getDayOfCurrentWeek;

@Controller
@Slf4j
@RequestMapping("/medical-tests")
@Validated
public class MedicalTestController extends DomainServiceController {

    private final MedicalTestService medicalTestService;

    @Autowired
    public MedicalTestController(AuthDataExtractor authDataExtractor, AccessGuard accessGuard, MedicalTestService medicalTestService) {
        super(authDataExtractor, accessGuard);
        this.medicalTestService = medicalTestService;
    }

    /***
     * MedicalTest can be created only by Patient and Receptionist which belongs to given department.
     */
    @PostMapping
    public ResponseEntity<MedicalTestRS> createMedicalTestVisit(@RequestBody @Valid MedicalTestRQ medicalTestRQ) throws Exception {
        try {
            if (!medicalTestRQ.getPatientUUID().equals(authDataExtractor.getId()) && !hasReceptionistAccessToDepartment(medicalTestRQ.getDepartmentId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            MedicalTestRS medicalTest = medicalTestService.createMedicalTest(medicalTestRQ);
            return ResponseEntity.ok(medicalTest);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (OverwritingTimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @GetMapping
    @HasRoleReceptionist
    public ResponseEntity<Collection<MedicalTestRS>> getMedicalTestByDepartment(@RequestParam Long departmentId,
                                                                                @RequestParam(required = false) TestType testType,
                                                                                @RequestParam(required = false) LocalDateTime startDateTime,
                                                                                @RequestParam(required = false) LocalDateTime endDateTime) {
        if (!hasReceptionistAccessToDepartment(departmentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (startDateTime != null && endDateTime != null && startDateTime.isAfter(endDateTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (startDateTime == null || endDateTime == null) {
            startDateTime = getDayOfCurrentWeek(DayOfWeek.MONDAY);
            endDateTime = getDayOfCurrentWeek(DayOfWeek.SUNDAY);
        }
        if (testType == null) {
            return ResponseEntity.ok(medicalTestService.getMedicalTestsByDepartmentId(departmentId, startDateTime, endDateTime));
        }
        return ResponseEntity.ok(medicalTestService.getMedicalTestsByTypeAndDepartmentId(testType, departmentId, startDateTime, endDateTime));
    }

    @GetMapping("/by-patientId")
    public ResponseEntity<Collection<MedicalTestRS>> getMedicalTestByPatientId(@RequestParam UUID patientUUID) {
        try {
            if (isPatient() && !Objects.equals(patientUUID, authDataExtractor.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(medicalTestService.getAllMedicalByPatientId(patientUUID));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("by-id/{id}")
    public ResponseEntity<MedicalTestRS> getMedicalTestById(@PathVariable Long id) {
        Optional<MedicalTestRS> medicalTestById =
                medicalTestService.getMedicalTestById(id);
        if (medicalTestById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            if (!isPatientOwnerOfTest(medicalTestById.get()) || !hasReceptionistAccessToDepartment(medicalTestById.get().getDepartmentId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(medicalTestById.get());
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * MedicalTest can be deleted only by Patient and Receptionist which belongs to given department.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicalTest(@PathVariable Long id) {
        Optional<MedicalTestRS> medicalTestById =
                medicalTestService.getMedicalTestById(id);
        if (medicalTestById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            if (!isPatientOwnerOfTest(medicalTestById.get()) || !hasReceptionistAccessToDepartment(medicalTestById.get().getDepartmentId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            medicalTestService.cancelMedicalTest(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    private boolean isPatientOwnerOfTest(MedicalTestRS medicalTestRS) throws AuthException {
        return Objects.equals(medicalTestRS.getPatientUUID(), authDataExtractor.getId());
    }
}
