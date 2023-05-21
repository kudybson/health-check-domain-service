package pl.akh.domainservicesvc.domain.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.AdministratorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PasswordConfirmationException;
import pl.akh.domainservicesvc.domain.exceptions.UsernameOrEmailAlreadyExistsException;
import pl.akh.domainservicesvc.domain.services.AccessGuard;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleAdmin;
import pl.akh.model.rq.ReceptionistRQ;
import pl.akh.model.rs.AdministratorRS;
import pl.akh.model.rs.DepartmentRS;
import pl.akh.model.rs.ReceptionistRS;
import pl.akh.services.AdministratorService;
import pl.akh.services.ReceptionistService;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/receptionists")
@Slf4j
@Validated
public class ReceptionistController extends DomainServiceController {

    private final ReceptionistService receptionistService;

    private final AdministratorService administratorService;

    public ReceptionistController(AuthDataExtractor authDataExtractor, AccessGuard accessGuard, ReceptionistService receptionistService, AdministratorService administratorService) {
        super(authDataExtractor, accessGuard);
        this.receptionistService = receptionistService;
        this.administratorService = administratorService;
    }

    @GetMapping(path = "/{uuid}")
    public ResponseEntity<ReceptionistRS> getReceptionistByUUID(@PathVariable @NotNull UUID uuid) {
        try {
            Optional<ReceptionistRS> receptionist = receptionistService.getReceptionistByUUID(uuid);
            if (receptionist.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (!hasAdministrativeAccessToDepartment(receptionist.get().getDepartmentId()) && !Objects.equals(authDataExtractor.getId(), uuid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return receptionist.map(ResponseEntity::ok).get();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @HasRoleAdmin
    @PostMapping
    public ResponseEntity<ReceptionistRS> createReceptionist(@RequestBody @Valid ReceptionistRQ receptionistRQ) {
        if (!hasAdministrativeAccessToDepartment(receptionistRQ.getDepartmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            ReceptionistRS receptionistRS = receptionistService.createReceptionist(receptionistRQ);
            return ResponseEntity.ok(receptionistRS);
        } catch (DepartmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UsernameOrEmailAlreadyExistsException | UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (PasswordConfirmationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @HasRoleAdmin
    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<String> deleteReceptionistByUUID(@PathVariable @NotNull UUID uuid) {
        try {
            Optional<ReceptionistRS> receptionist = receptionistService.getReceptionistByUUID(uuid);
            if (receptionist.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (!hasAdministrativeAccessToDepartment(receptionist.get().getDepartmentId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            receptionistService.deleteReceptionist(uuid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @HasRoleAdmin
    @GetMapping
    public ResponseEntity<Collection<ReceptionistRS>> getReceptionists() {
        try {
            AdministratorRS administrator = administratorService.getAdministratorById(authDataExtractor.getId())
                    .orElseThrow();
            Long departmentId = administrator.getDepartmentId();
            Collection<ReceptionistRS> receptionists = receptionistService.getReceptionistsByDepartmentId(departmentId);
            return ResponseEntity.ok(receptionists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/departments/{uuid}")
    public ResponseEntity<DepartmentRS> getDepartmentByReceptionistId(@PathVariable UUID uuid) throws Exception {
        try {
            return ResponseEntity.ok(receptionistService.getDepartmentByReceptionistId(uuid));
        } catch (AdministratorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
