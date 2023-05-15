package pl.akh.domainservicesvc.domain.controllers;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.AppointmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.TreatmentNotFoundException;
import pl.akh.domainservicesvc.domain.model.entities.Appointment;
import pl.akh.domainservicesvc.domain.model.entities.Treatment;
import pl.akh.domainservicesvc.domain.repository.AppointmentRepository;
import pl.akh.domainservicesvc.domain.repository.TreatmentRepository;
import pl.akh.domainservicesvc.domain.services.AccessGuard;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleDoctor;
import pl.akh.model.rq.PrescriptionRQ;
import pl.akh.model.rq.ReferralRQ;
import pl.akh.model.rq.TreatmentRQ;
import pl.akh.model.rq.UpdateTreatmentRQ;
import pl.akh.model.rs.TreatmentRS;
import pl.akh.services.TreatmentService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/treatments")
@Slf4j
@Validated
public class TreatmentController extends DomainServiceController {

    private final TreatmentService treatmentService;
    private final TreatmentRepository treatmentRepository;
    private final AppointmentRepository appointmentRepository;

    public TreatmentController(AuthDataExtractor authDataExtractor, AccessGuard accessGuard, TreatmentService treatmentService, TreatmentRepository treatmentRepository, AppointmentRepository appointmentRepository) {
        super(authDataExtractor, accessGuard);
        this.treatmentService = treatmentService;
        this.treatmentRepository = treatmentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @PostMapping
    @HasRoleDoctor
    public ResponseEntity<TreatmentRS> addTreatmentToAppointment(@RequestBody @Valid TreatmentRQ treatmentRQ) throws Exception {
        if (!checkIfAppointmentIsWithThisDoctorByAppointmentId(treatmentRQ.getAppointmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(treatmentService.addTreatmentToAppointment(treatmentRQ));
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @HasRoleDoctor
    @PutMapping("/{id}")
    public ResponseEntity<TreatmentRS> updateTreatment(@PathVariable Long id, @RequestBody @Valid UpdateTreatmentRQ treatmentRQ) throws Exception {
        if (!checkIfAppointmentIsWithThisDoctorByTreatmentId(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(treatmentService.updateTreatment(id, treatmentRQ));
        } catch (TreatmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @HasRoleDoctor
    @DeleteMapping("/appointment/{appointmentId}")
    public ResponseEntity<String> removeTreatmentByAppointmentId(@PathVariable Long appointmentId) throws Exception {
        if (!checkIfAppointmentIsWithThisDoctorByAppointmentId(appointmentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        treatmentService.removeTreatmentByAppointmentId(appointmentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @HasRoleDoctor
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTreatment(@PathVariable Long id) throws Exception {
        if (!checkIfAppointmentIsWithThisDoctorByTreatmentId(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Treatment> treatmentById = treatmentRepository.findById(id);
        if (treatmentById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        treatmentService.deleteTreatment(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("prescription")
    @HasRoleDoctor
    public ResponseEntity<String> addPrescription(@RequestBody @Valid PrescriptionRQ prescriptionRQ) throws Exception {
        if (!checkIfAppointmentIsWithThisDoctorByTreatmentId(prescriptionRQ.getTreatmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            treatmentService.addPrescription(prescriptionRQ);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (TreatmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("referral")
    @HasRoleDoctor
    public ResponseEntity<String> addReferral(@RequestBody @Valid ReferralRQ referralRQ) throws Exception {
        if (!checkIfAppointmentIsWithThisDoctorByTreatmentId(referralRQ.getTreatmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            treatmentService.addReferral(referralRQ);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (TreatmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public boolean checkIfAppointmentIsWithThisDoctorByAppointmentId(Long appointmentId) throws AuthException {
        UUID loggedId = authDataExtractor.getId();
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isPresent() && !optionalAppointment.get().getDoctor().getId().equals(loggedId)) {
            return false;
        } else return true;
    }

    public boolean checkIfAppointmentIsWithThisDoctorByTreatmentId(Long treatmentId) throws AuthException {
        Optional<Treatment> optionalTreatment = treatmentRepository.findById(treatmentId);
        if (optionalTreatment.isPresent()) {
            return checkIfAppointmentIsWithThisDoctorByAppointmentId(optionalTreatment.get().getAppointment().getId());
        } else return true;
    }
}
