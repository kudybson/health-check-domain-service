package pl.akh.domainservicesvc.domain.controllers;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.akh.domainservicesvc.domain.exceptions.AdministratorNotFoundException;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleAdmin;
import pl.akh.domainservicesvc.domain.utils.roles.HasRoleSuperAdmin;
import pl.akh.domainservicesvc.domain.utils.roles.Public;
import pl.akh.model.rq.DepartmentRQ;
import pl.akh.model.rs.DepartmentRS;
import pl.akh.services.DepartmentService;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/departments")
@Slf4j
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Public
    @GetMapping
    public ResponseEntity<Collection<DepartmentRS>> getDepartmentsByCriteria(@RequestParam(required = false) String city,
                                                                             @RequestParam(required = false) String street,
                                                                             @RequestParam(required = false) String county,
                                                                             @RequestParam(required = false) String province,
                                                                             @RequestParam(required = false) String country) {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @Public
    @GetMapping(path = "/{id}")
    public ResponseEntity<DepartmentRS> getDepartmentById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping(path = "/administrator/{uuid}")
    @HasRoleAdmin
    public ResponseEntity<DepartmentRS> getDepartmentByAdministratorId(@PathVariable UUID uuid) throws Exception {
        try {
            return ResponseEntity.ok(departmentService.getDepartmentByAdministratorId(uuid));
        } catch (AdministratorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    @HasRoleSuperAdmin
    public ResponseEntity<DepartmentRS> createDepartment(@RequestBody @Valid DepartmentRQ departmentRQ) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(departmentRQ));
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    @HasRoleSuperAdmin
    public ResponseEntity<DepartmentRS> deleteDepartmentById(@PathVariable Long id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
