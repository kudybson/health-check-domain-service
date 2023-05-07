package pl.akh.domainservicesvc.domain.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PatientNotFoundException;
import pl.akh.domainservicesvc.domain.services.AccessGuard;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.model.rq.RateRQ;
import pl.akh.services.RatingService;

@RestController
@RequestMapping("/ratings")
@Validated
public class RatingController extends DomainServiceController {

    private final RatingService ratingService;

    public RatingController(AuthDataExtractor authDataExtractor, AccessGuard accessGuard, RatingService ratingService) {
        super(authDataExtractor, accessGuard);
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<String> addRatingToDoctor(@RequestBody @Valid RateRQ rateRQ) throws Exception {
        try {
            if (!isPatient()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            ratingService.addRatingToDoctor(rateRQ);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (PatientNotFoundException | DoctorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
