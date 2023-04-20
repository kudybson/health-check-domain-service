package pl.akh.domainservicesvc.domain.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PasswordConfirmationException;
import pl.akh.domainservicesvc.domain.exceptions.UsernameOrEmailAlreadyExistsException;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleAdmin;
import pl.akh.model.rq.ReceptionistRQ;
import pl.akh.model.rs.ReceptionistRS;
import pl.akh.services.ReceptionistService;

import java.util.UUID;

@RestController
@RequestMapping("/receptionists")
@Slf4j
@Validated
public class ReceptionistController {

    private final ReceptionistService receptionistService;


    public ReceptionistController(ReceptionistService receptionistService) {
        this.receptionistService = receptionistService;
    }

    @HasRoleAdmin
    @GetMapping(path = "/{uuid}")
    public ResponseEntity<ReceptionistRS> getReceptionistByUUID(@PathVariable @NotNull UUID uuid) {
        try {
            return receptionistService.getReceptionistByUUID(uuid)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @HasRoleAdmin
    @PostMapping
    public ResponseEntity<ReceptionistRS> createReceptionist(@RequestBody @Valid ReceptionistRQ receptionistRQ) {
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
            receptionistService.deleteReceptionist(uuid);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
