package pl.akh.domainservicesvc.domain.services.api;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PatientNotFoundException;
import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.domainservicesvc.domain.model.entities.Patient;
import pl.akh.domainservicesvc.domain.model.entities.Rating;
import pl.akh.domainservicesvc.domain.repository.DoctorRepository;
import pl.akh.domainservicesvc.domain.repository.PatientRepository;
import pl.akh.domainservicesvc.domain.repository.RatingRepository;
import pl.akh.model.rq.RateRQ;
import pl.akh.services.RatingService;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository, RatingRepository ratingRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public void addRatingToDoctor(RateRQ rateRQ) throws DoctorNotFoundException, PatientNotFoundException {
        Doctor doctor = doctorRepository.findById(rateRQ.getDoctorUUID())
                .orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id: %s not found.", rateRQ.getDoctorUUID())));
        Patient patient = patientRepository.findById(rateRQ.getPatientUUID())
                .orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id: %s not found.", rateRQ.getPatientUUID())));

        Rating rating = new Rating();
        rating.setDoctor(doctor);
        rating.setPatient(patient);
        rating.setGrade(rateRQ.getGrade());
        rating.setDescription(rateRQ.getDescription());
        ratingRepository.save(rating);
    }
}
