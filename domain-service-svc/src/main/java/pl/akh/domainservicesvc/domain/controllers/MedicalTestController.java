package pl.akh.domainservicesvc.domain.controllers;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import jdk.jfr.Description;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.akh.domainservicesvc.domain.exceptions.OverwritingTimeException;
import pl.akh.domainservicesvc.domain.services.AccessGuard;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleReceptionist;
import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestRQ;
import pl.akh.model.rs.MedicalTestRS;
import pl.akh.services.MedicalTestService;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_PDF;
import static pl.akh.domainservicesvc.domain.utils.DateUtils.getDayOfCurrentWeek;

@RestController
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

    @GetMapping("/department/{departmentId}")
    @HasRoleReceptionist
    public ResponseEntity<Collection<MedicalTestRS>> getMedicalTestByDepartment(@PathVariable Long departmentId,
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

    @GetMapping("/patient/{patientUUID}")
    public ResponseEntity<Collection<MedicalTestRS>> getMedicalTestByPatientId(@PathVariable UUID patientUUID) {
        try {
            if (isPatient() && !Objects.equals(patientUUID, authDataExtractor.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(medicalTestService.getAllMedicalByPatientId(patientUUID));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<MedicalTestRS> getMedicalTestById(@PathVariable Long id) {
        Optional<MedicalTestRS> medicalTestById =
                medicalTestService.getMedicalTestById(id);
        if (medicalTestById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            if (!medicalTestById.get().getPatientUUID().equals(authDataExtractor.getId()) && isPatient()) {
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
    public ResponseEntity<String> deleteMedicalTest(@PathVariable Long id) throws Exception {
        Optional<MedicalTestRS> medicalTestById =
                medicalTestService.getMedicalTestById(id);
        if (medicalTestById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            if (!isPatientOwnerOfTest(medicalTestById.get()) && !hasReceptionistAccessToDepartment(medicalTestById.get().getDepartmentId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            medicalTestService.cancelMedicalTest(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping(value = "/result/{medicalTestId}", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getMedicalTestResult(@PathVariable Long medicalTestId) {
        Optional<MedicalTestRS> medicalTest = medicalTestService.getMedicalTestById(medicalTestId);
        if (medicalTest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            if (!medicalTest.get().getPatientUUID().equals(authDataExtractor.getId()) && isPatient()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok()
                    .contentType(APPLICATION_PDF)
                    .body(medicalTestService.getResultByMedicalTestId(medicalTestId));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/result/{medicalTestId}")
    @HasRoleReceptionist
    public ResponseEntity<String> createMedicalTestResult(@RequestParam("file") MultipartFile file,
                                                          @PathVariable Long medicalTestId) {
        Optional<MedicalTestRS> medicalTest = medicalTestService.getMedicalTestById(medicalTestId);
        if (medicalTest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!hasReceptionistAccessToDepartment(medicalTest.get().getDepartmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            medicalTestService.addMedicalResult(file, medicalTestId);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            log.error("Error during medical test result creation: ", e);
            return ResponseEntity.status(404).build();
        }
    }

    private boolean isPatientOwnerOfTest(MedicalTestRS medicalTestRS) throws AuthException {
        return Objects.equals(medicalTestRS.getPatientUUID(), authDataExtractor.getId());
    }
}
