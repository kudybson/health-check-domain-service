package pl.akh.domainservicesvc.domain.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.AppointmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.TreatmentNotFoundException;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleDoctor;
import pl.akh.model.rq.TreatmentRQ;
import pl.akh.model.rq.UpdateTreatmentRQ;
import pl.akh.model.rs.TreatmentRS;
import pl.akh.services.TreatmentService;

@RestController
@RequestMapping("/treatments")
@Slf4j
@Validated
public class TreatmentController {

    private final TreatmentService treatmentService;

    @Autowired
    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @PostMapping
    @HasRoleDoctor
    public ResponseEntity<TreatmentRS> addTreatmentToAppointment(@RequestBody @Valid TreatmentRQ treatmentRQ) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(treatmentService.addTreatmentToAppointment(treatmentRQ));
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @HasRoleDoctor
    @PutMapping("/{id}")
    public ResponseEntity<TreatmentRS> updateTreatment(@PathVariable Long id, @RequestBody @Valid UpdateTreatmentRQ treatmentRQ) throws Exception {
        try {
            return ResponseEntity.ok(treatmentService.updateTreatment(id, treatmentRQ));
        } catch (TreatmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //todo chyba do zrobienia to, żeby ten doktor, który mof=dyfikuje sprawdzac czy to jego appointment
    @HasRoleDoctor
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<String> removeTreatmentByAppointmentId(@PathVariable Long appointmentId) throws Exception {
        try {
            treatmentService.removeTreatmentByAppointmentId(appointmentId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (TreatmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
