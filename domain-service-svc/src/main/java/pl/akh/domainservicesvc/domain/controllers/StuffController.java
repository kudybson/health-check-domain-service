package pl.akh.domainservicesvc.domain.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleAdmin;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleSuperAdmin;
import pl.akh.model.rq.AdministratorRQ;
import pl.akh.model.rq.ReceptionistRQ;
import pl.akh.model.rs.AdministratorRS;
import pl.akh.model.rs.ReceptionistRS;
import pl.akh.services.StaffService;

import javax.ws.rs.PathParam;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/stuff")
@Validated
public class StuffController {

    private StaffService staffService;

    @Autowired
    public StuffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @HasRoleSuperAdmin
    @GetMapping("/administrators")
    public ResponseEntity<AdministratorRS> getAdministratorByUUID(@PathParam(value = "adminUUID") UUID adminUUID,
                                                                  @PathParam(value = "departmentId") Long departmentId) {
        return null;
    }

    @HasRoleSuperAdmin
    @PostMapping("/administrators")
    public ResponseEntity<AdministratorRS> createAdministrator(@RequestBody AdministratorRQ administratorRQ) {
        return null;
    }

    @HasRoleAdmin
    @PostMapping("/receptionists")
    public ResponseEntity<AdministratorRS> getReceptionist(@PathParam(value = "receptionistUUID") UUID receptionistUUID) {
        return null;
    }

    @HasRoleAdmin
    @PostMapping("/receptionist")
    public ResponseEntity<AdministratorRS> createReceptionist(@RequestBody ReceptionistRQ receptionistRQ) {
        return null;
    }
}
