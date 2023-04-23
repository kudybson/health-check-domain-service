package pl.akh.domainservicesvc.domain.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PasswordConfirmationException;
import pl.akh.domainservicesvc.domain.exceptions.UsernameOrEmailAlreadyExistsException;
import pl.akh.domainservicesvc.domain.services.AccessService;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleAdmin;
import pl.akh.domainservicesvc.domain.utils.roles.Public;
import pl.akh.model.common.Specialization;
import pl.akh.model.rq.DoctorRQ;
import pl.akh.model.rs.DoctorRS;
import pl.akh.model.rs.RatingRS;
import pl.akh.services.DoctorService;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/doctors")
@Validated
public class DoctorController extends DomainServiceController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(AuthDataExtractor authDataExtractor, AccessService accessService, DoctorService doctorService) {
        super(authDataExtractor, accessService);
        this.doctorService = doctorService;
    }

    @Public
    @GetMapping
    public ResponseEntity<Collection<DoctorRS>> getAllDoctors(@RequestParam(required = false) Specialization specialization,
                                                              @RequestParam(required = false) Long departmentId,
                                                              @RequestParam(required = false) String firstName,
                                                              @RequestParam(required = false) String lastName) {
        return ResponseEntity.ok(doctorService.getDoctorsByCriteria(specialization, departmentId, firstName, lastName));
    }

    @Public
    @GetMapping("/{uuid}")
    public ResponseEntity<DoctorRS> getDoctorById(@PathVariable UUID uuid) {
        return doctorService.getDoctorById(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @HasRoleAdmin
    @PostMapping
    public ResponseEntity<DoctorRS> createDoctor(@RequestBody @Valid DoctorRQ doctorRQ) throws Exception {
        if (!hasAccessAdministrationAccessToDepartment(doctorRQ.getDepartmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            DoctorRS doctorRS = doctorService.createDoctor(doctorRQ);
            return ResponseEntity.ok(doctorRS);
        } catch (DepartmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UsernameOrEmailAlreadyExistsException | UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (PasswordConfirmationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @HasRoleAdmin
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable UUID uuid) throws Exception {
        Optional<DoctorRS> doctorById = doctorService.getDoctorById(uuid);
        if (doctorById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!hasAccessAdministrationAccessToDepartment(doctorById.get().getDepartmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        doctorService.deleteDoctor(uuid);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @Public
    @GetMapping("/{uuid}/rates")
    public ResponseEntity<Collection<RatingRS>> getDoctorRatesById(@PathVariable UUID uuid) throws Exception {
        try {
            return ResponseEntity.ok(doctorService.getDoctorRates(uuid));
        } catch (DoctorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}