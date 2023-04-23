package pl.akh.domainservicesvc.domain.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.akh.services.PatientService;

@RestController
@RequestMapping("/patients")
@Slf4j
@Validated
public class PatientController {

    private PatientService patientService;


}
