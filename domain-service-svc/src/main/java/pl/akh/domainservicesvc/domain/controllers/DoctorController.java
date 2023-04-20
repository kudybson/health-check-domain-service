package pl.akh.domainservicesvc.domain.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFountException;
import pl.akh.domainservicesvc.domain.exceptions.PasswordConfirmationException;
import pl.akh.domainservicesvc.domain.exceptions.UsernameOrEmailAlreadyExistsException;
import pl.akh.domainservicesvc.domain.services.AccessService;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleAdmin;
import pl.akh.domainservicesvc.domain.utils.roles.Public;
import pl.akh.model.common.Specialization;
import pl.akh.model.rq.DoctorRQ;
import pl.akh.model.rs.AdministratorRS;
import pl.akh.model.rs.DoctorRS;
import pl.akh.services.DoctorService;

import java.util.Collection;
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
    @GetMapping("/uuid")
    public ResponseEntity<DoctorRS> getDoctorById(@PathVariable UUID uuid) {
        return doctorService.getDoctorById(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @HasRoleAdmin
    @PostMapping
    public ResponseEntity<DoctorRS> createDoctor(@RequestBody @Valid DoctorRQ doctorRQ) {
        if (!hasAccessAdministrationAccessToDepartment(doctorRQ.getDepartmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            DoctorRS doctorRS = doctorService.createDoctor(doctorRQ);
            return ResponseEntity.ok(doctorRS);
        } catch (DepartmentNotFountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UsernameOrEmailAlreadyExistsException | UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (PasswordConfirmationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}