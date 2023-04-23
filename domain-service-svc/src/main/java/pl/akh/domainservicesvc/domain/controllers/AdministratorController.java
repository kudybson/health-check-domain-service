package pl.akh.domainservicesvc.domain.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PasswordConfirmationException;
import pl.akh.domainservicesvc.domain.exceptions.UsernameOrEmailAlreadyExistsException;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleSuperAdmin;
import pl.akh.model.rq.AdministratorRQ;
import pl.akh.model.rs.AdministratorRS;
import pl.akh.services.AdministratorService;

import java.util.UUID;


@RestController
@RequestMapping("/administrators")
@Slf4j
@Validated
public class AdministratorController {

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @HasRoleSuperAdmin
    @GetMapping(path = "/{uuid}")
    public ResponseEntity<AdministratorRS> getAdministratorByUUID(@PathVariable UUID uuid) {
        try {
            return administratorService.getAdministratorById(uuid)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @HasRoleSuperAdmin
    @GetMapping
    public ResponseEntity<AdministratorRS> getAdministratorByDepartmentId(@RequestParam Long departmentId) {
        try {
            return administratorService.getAdministratorByDepartmentId(departmentId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @HasRoleSuperAdmin
    @PostMapping
    public ResponseEntity<AdministratorRS> createAdministrator(@RequestBody AdministratorRQ administratorRQ) {
        try {
            AdministratorRS administratorRS = administratorService.addAdministrator(administratorRQ);
            return ResponseEntity.ok(administratorRS);
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

    @HasRoleSuperAdmin
    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<String> deleteAdministratorByUUID(@PathVariable UUID uuid) {
        try {
            administratorService.deleteAdministrator(uuid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
