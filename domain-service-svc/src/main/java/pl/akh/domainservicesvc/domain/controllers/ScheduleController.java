package pl.akh.domainservicesvc.domain.controllers;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.services.AccessGuard;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleDoctor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleReceptionist;
import pl.akh.domainservicesvc.domain.utils.roles.Public;
import pl.akh.model.rq.ScheduleRQ;
import pl.akh.model.rs.schedules.ScheduleRS;
import pl.akh.model.rs.schedules.SchedulesAppointmentsRS;
import pl.akh.services.ScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import static pl.akh.domainservicesvc.domain.utils.DateUtils.getDayOfCurrentWeek;

@RestController
@RequestMapping("schedules")
@Slf4j
public class ScheduleController extends DomainServiceController {

    private ScheduleService scheduleService;

    @Autowired
    public ScheduleController(AuthDataExtractor authDataExtractor, AccessGuard accessGuard, ScheduleService scheduleService) {
        super(authDataExtractor, accessGuard);
        this.scheduleService = scheduleService;
    }

    @Public
    @GetMapping("/{doctorUUID}")
    ResponseEntity<Collection<ScheduleRS>> getSchedules(@PathVariable UUID doctorUUID,
                                                        @RequestParam(required = false) LocalDateTime startDateTime,
                                                        @RequestParam(required = false) LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            startDateTime = getDayOfCurrentWeek(DayOfWeek.MONDAY);
            endDateTime = getDayOfCurrentWeek(DayOfWeek.SUNDAY);
        }
        try {
            Collection<ScheduleRS> schedulesByDoctorIdBetweenDates = scheduleService.getSchedulesByDoctorIdBetweenDates(doctorUUID, startDateTime, endDateTime);
            return ResponseEntity.ok(schedulesByDoctorIdBetweenDates);
        } catch (DoctorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Public
    @GetMapping("/with-appointments/{doctorUUID}")
    ResponseEntity<SchedulesAppointmentsRS> getSchedulesWithAppointments(@PathVariable UUID doctorUUID,
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
            SchedulesAppointmentsRS schedulesByDoctorIdBetweenDates = scheduleService.getSchedulesWithAppointmentByDoctorId(doctorUUID, startDateTime, endDateTime);
            return ResponseEntity.ok(schedulesByDoctorIdBetweenDates);
        } catch (DoctorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @HasRoleDoctor
    @PostMapping
    ResponseEntity<Collection<ScheduleRS>> addSchedules(@RequestBody Collection<ScheduleRQ> schedules) {
        try {
            UUID id = authDataExtractor.getId();
            Collection<ScheduleRS> scheduleRS = scheduleService.insertSchedules(id, schedules);
            return ResponseEntity.ok(scheduleRS);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @HasRoleReceptionist
    @PostMapping("doctor/{doctorUUID}")
    ResponseEntity<Collection<ScheduleRS>> addSchedulesByDoctorId(@PathVariable UUID doctorUUID, @RequestBody Collection<ScheduleRQ> schedules) {
        try {
            UUID id = authDataExtractor.getId();
            Collection<ScheduleRS> scheduleRS = scheduleService.insertSchedules(doctorUUID, schedules);
            return ResponseEntity.ok(scheduleRS);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
