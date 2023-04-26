package pl.akh.domainservicesvc.domain.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PatientNotFoundException;
import pl.akh.model.rq.AppointmentRQ;
import pl.akh.model.rs.AppointmentRS;
import pl.akh.services.AppointmentService;

@RestController
@RequestMapping("/appointments")
@Validated
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentRS> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<AppointmentRS> createAppointment(@RequestBody @Valid AppointmentRQ appointmentRQ) throws Exception {
        try {
            AppointmentRS appointmentRS = appointmentService.createAppointment(appointmentRQ);
            return ResponseEntity.ok(appointmentRS);
        } catch (PatientNotFoundException | DoctorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
