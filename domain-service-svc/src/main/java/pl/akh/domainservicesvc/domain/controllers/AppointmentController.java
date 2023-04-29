package pl.akh.domainservicesvc.domain.controllers;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.AppointmentConflictException;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PatientNotFoundException;
import pl.akh.domainservicesvc.domain.repository.DoctorRepository;
import pl.akh.domainservicesvc.domain.services.AccessGuard;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleDoctor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleReceptionist;
import pl.akh.model.rq.AppointmentRQ;
import pl.akh.model.rs.AppointmentRS;
import pl.akh.services.AppointmentService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static pl.akh.domainservicesvc.domain.utils.DateUtils.getDayOfCurrentWeek;

@RestController
@RequestMapping("/appointments")
@Validated
public class AppointmentController extends DomainServiceController {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;

    public AppointmentController(AuthDataExtractor authDataExtractor, AccessGuard accessGuard, AppointmentService appointmentService, DoctorRepository doctorRepository) {
        super(authDataExtractor, accessGuard);
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentRS> getAppointmentById(@PathVariable Long id) throws AuthException {
        Optional<AppointmentRS> appointment = appointmentService.getAppointmentById(id);
        if (appointment.isPresent()) {
            if ((isDoctor() && !isThisAppointmentToThisDoctor(appointment.get().getDoctorRS().getDoctorUUID())) ||
                    (isPatient() && !isThisAppointmentIsForThisPatient(appointment.get().getPatientRS().getPatientUUID())) ||
                    (isReceptionist() && !hasReceptionistAccessToDepartment(appointment.get().getDepartmentId()))
                    || isAdmin() || isSuperAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return appointment
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<AppointmentRS> createAppointment(@RequestBody @Valid AppointmentRQ appointmentRQ) throws Exception {
        try {
            if ((isPatient() && !isThisAppointmentIsForThisPatient(appointmentRQ.getPatientUUID())) ||
                    (isReceptionist() && !hasReceptionistAccessToDepartment(doctorRepository.getDepartmentIdByDoctorId(appointmentRQ.getDoctorUUID())))
                    || isDoctor() || isAdmin() || isSuperAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(appointmentService.createAppointment(appointmentRQ));
        } catch (PatientNotFoundException | DoctorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AppointmentConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/doctor/{doctorUUID}")
    public ResponseEntity<Collection<AppointmentRS>> getAppointmentsByDoctorId(@PathVariable UUID doctorUUID,
                                                                               @RequestParam(required = false) LocalDateTime startDateTime,
                                                                               @RequestParam(required = false) LocalDateTime endDateTime) throws AuthException {
        if (isWrongDates(startDateTime, endDateTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (startDateTime == null || endDateTime == null) {
            startDateTime = getStartDateTime();
            endDateTime = getEndDateTime();
        }
        if ((isDoctor() && !isThisAppointmentToThisDoctor(doctorUUID)) ||
                (isReceptionist() && !hasReceptionistAccessToDepartment(doctorRepository.getDepartmentIdByDoctorId(doctorUUID)))
                || isPatient() || isAdmin() || isSuperAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorUUID, startDateTime, endDateTime));
    }

    @GetMapping("/patient/{patientUUID}")
    public ResponseEntity<Collection<AppointmentRS>> getAppointmentsByPatientId(@PathVariable UUID patientUUID,
                                                                                @RequestParam(required = false) LocalDateTime startDateTime,
                                                                                @RequestParam(required = false) LocalDateTime endDateTime) throws AuthException {
        if (!isPatient()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (isWrongDates(startDateTime, endDateTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (startDateTime == null || endDateTime == null) {
            startDateTime = getStartDateTime();
            endDateTime = getEndDateTime();
        }
        if (!isThisAppointmentIsForThisPatient(patientUUID)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientUUID, startDateTime, endDateTime));
    }

    @HasRoleReceptionist
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Collection<AppointmentRS>> getAppointmentsByDepartmentId(@PathVariable Long departmentId,
                                                                                   @RequestParam(required = false) LocalDateTime startDateTime,
                                                                                   @RequestParam(required = false) LocalDateTime endDateTime) {
        if (isWrongDates(startDateTime, endDateTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (startDateTime == null || endDateTime == null) {
            startDateTime = getStartDateTime();
            endDateTime = getEndDateTime();
        }
        if (!hasReceptionistAccessToDepartment(departmentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(appointmentService.getAppointmentsByDepartmentId(departmentId, startDateTime, endDateTime));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeAppointmentById(@PathVariable Long id) throws Exception {
        Optional<AppointmentRS> appointment = appointmentService.getAppointmentById(id);
        if (appointment.isPresent()) {
            if ((isDoctor() && !isThisAppointmentToThisDoctor(appointment.get().getDoctorRS().getDoctorUUID())) ||
                    (isPatient() && !isThisAppointmentIsForThisPatient(appointment.get().getPatientRS().getPatientUUID())) ||
                    (isReceptionist() && !hasReceptionistAccessToDepartment(appointment.get().getDepartmentId()))
                    || isAdmin() || isSuperAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            appointmentService.removeAppointmentById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @HasRoleDoctor
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentRS> addCommentToAppointment(@PathVariable Long id, @RequestParam String comment) throws AuthException {

        Optional<AppointmentRS> appointment = appointmentService.getAppointmentById(id);
        if (appointment.isPresent()) {
            if (!isThisAppointmentToThisDoctor(appointment.get().getDoctorRS().getDoctorUUID())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            return ResponseEntity.ok(appointmentService.addCommentToAppointment(id, comment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isThisAppointmentToThisDoctor(UUID doctorUUID) throws AuthException {
        return doctorUUID.equals(authDataExtractor.getId());
    }

    private boolean isThisAppointmentIsForThisPatient(UUID patientUUID) throws AuthException {
        return patientUUID.equals(authDataExtractor.getId());
    }

    private boolean isWrongDates(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime != null && endDateTime != null && startDateTime.isAfter(endDateTime);
    }

    private LocalDateTime getStartDateTime() {
        return getDayOfCurrentWeek(DayOfWeek.MONDAY);
    }

    private LocalDateTime getEndDateTime() {
        return getDayOfCurrentWeek(DayOfWeek.SUNDAY).plusDays(1);
    }
}
