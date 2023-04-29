package pl.akh.domainservicesvc.domain.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.services.AccessGuard;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleReceptionist;
import pl.akh.domainservicesvc.domain.utils.roles.Public;
import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestScheduleRQ;
import pl.akh.model.rs.schedules.MedicalTestSchedulesRS;
import pl.akh.model.rs.schedules.ScheduleRS;
import pl.akh.services.MedicalTestScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collection;

import static pl.akh.domainservicesvc.domain.utils.DateUtils.getDayOfCurrentWeek;

@RestController
@RequestMapping("/medical-tests-schedules")
@Slf4j
@Validated
public class MedicalTestSchedulesController extends DomainServiceController {

    private final MedicalTestScheduleService medicalTestScheduleService;

    @Autowired
    public MedicalTestSchedulesController(AuthDataExtractor authDataExtractor, AccessGuard accessGuard, MedicalTestScheduleService medicalTestScheduleService) {
        super(authDataExtractor, accessGuard);
        this.medicalTestScheduleService = medicalTestScheduleService;
    }

    @PostMapping
    @HasRoleReceptionist
    public ResponseEntity<Collection<ScheduleRS>> insertSchedules(@Valid @RequestBody MedicalTestScheduleRQ medicalTestScheduleRQ) {
        if (!hasReceptionistAccessToDepartment(medicalTestScheduleRQ.getDepartmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Collection<ScheduleRS> scheduleRS = medicalTestScheduleService.insertMedicalTestSchedules(medicalTestScheduleRQ);
            return ResponseEntity.ok(scheduleRS);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    @Public
    public ResponseEntity<MedicalTestSchedulesRS> getMedicalTestSchedules(@RequestParam Long departmentId, @RequestParam TestType testType,
                                                                          @RequestParam(required = false) LocalDateTime startDateTime,
                                                                          @RequestParam(required = false) LocalDateTime endDateTime) {
        if (startDateTime != null && endDateTime != null && startDateTime.isAfter(endDateTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (startDateTime == null || endDateTime == null) {
            startDateTime = getDayOfCurrentWeek(DayOfWeek.MONDAY);
            endDateTime = getDayOfCurrentWeek(DayOfWeek.SUNDAY);
        }
        try {
            MedicalTestSchedulesRS medicalTestSchedules = medicalTestScheduleService.getMedicalTestSchedules(departmentId, testType, startDateTime, endDateTime);
            return ResponseEntity.ok(medicalTestSchedules);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}