package pl.akh.domainservicesvc.domain.services.api;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.exceptions.AppointmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.TreatmentNotFoundException;
import pl.akh.domainservicesvc.domain.mappers.TestTypeMapper;
import pl.akh.domainservicesvc.domain.mappers.TreatmentMapper;
import pl.akh.domainservicesvc.domain.model.entities.Appointment;
import pl.akh.domainservicesvc.domain.model.entities.Prescription;
import pl.akh.domainservicesvc.domain.model.entities.Referral;
import pl.akh.domainservicesvc.domain.model.entities.Treatment;
import pl.akh.domainservicesvc.domain.repository.AppointmentRepository;
import pl.akh.domainservicesvc.domain.repository.PrescriptionRepository;
import pl.akh.domainservicesvc.domain.repository.ReferralRepository;
import pl.akh.domainservicesvc.domain.repository.TreatmentRepository;
import pl.akh.model.rq.PrescriptionRQ;
import pl.akh.model.rq.ReferralRQ;
import pl.akh.model.rq.TreatmentRQ;
import pl.akh.model.rq.UpdateTreatmentRQ;
import pl.akh.model.rs.TreatmentRS;
import pl.akh.services.TreatmentService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final ReferralRepository referralRepository;

    public TreatmentServiceImpl(TreatmentRepository treatmentRepository, AppointmentRepository appointmentRepository, PrescriptionRepository prescriptionRepository, ReferralRepository referralRepository) {
        this.treatmentRepository = treatmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.referralRepository = referralRepository;
    }

    @Override
    public TreatmentRS addTreatmentToAppointment(TreatmentRQ treatmentRQ) throws AppointmentNotFoundException {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(treatmentRQ.getAppointmentId());
        if (optionalAppointment.isEmpty()) throw new AppointmentNotFoundException();
        Appointment appointment = optionalAppointment.get();
        Treatment treatment = new Treatment();
        treatment.setAppointment(appointment);
        treatment.setDiagnosis(treatmentRQ.getDiagnosis());
        treatment.setRecommendation(treatmentRQ.getRecommendation());
        appointment.setTreatment(treatment);
        Treatment saved = treatmentRepository.save(treatment);
        appointmentRepository.save(appointment);
        return TreatmentMapper.mapToDto(saved);
    }

    @Override
    public TreatmentRS updateTreatment(long treatmentId, UpdateTreatmentRQ treatmentRQ) throws TreatmentNotFoundException {
        Optional<Treatment> optionalTreatment = treatmentRepository.findById(treatmentId);
        if (optionalTreatment.isEmpty()) throw new TreatmentNotFoundException();
        Treatment treatment = optionalTreatment.get();
        treatment.setRecommendation(treatmentRQ.getRecommendation());
        treatment.setDiagnosis(treatmentRQ.getDiagnosis());
        Treatment saved = treatmentRepository.save(treatment);
        return TreatmentMapper.mapToDto(saved);
    }

    @Override
    public void removeTreatmentByAppointmentId(long appointmentId) throws TreatmentNotFoundException {
        Optional<Treatment> optionalTreatment = treatmentRepository.findTreatmentByAppointmentId(appointmentId);
        if (optionalTreatment.isEmpty()) throw new TreatmentNotFoundException();
        treatmentRepository.delete(optionalTreatment.get());
    }

    @Override
    public void deleteTreatment(long treatmentId) {
        treatmentRepository.deleteById(treatmentId);
    }

    @Override
    public void addPrescription(PrescriptionRQ prescriptionRQ) throws TreatmentNotFoundException {
        Optional<Treatment> optionalTreatment = treatmentRepository.findTreatmentByAppointmentId(prescriptionRQ.getTreatmentId());
        if (optionalTreatment.isEmpty()) throw new TreatmentNotFoundException();
        Treatment treatment = optionalTreatment.get();
        Prescription prescription = new Prescription();
        prescription.setTreatment(treatment);
        prescription.setDescription(prescriptionRQ.getDescription());
        prescription.setCode(prescriptionRQ.getCode());
        prescription.setExpirationDate(Timestamp.valueOf(LocalDateTime.now().plusMonths(1)));
        prescriptionRepository.save(prescription);
        treatment.setPrescription(prescription);
        treatmentRepository.save(treatment);
    }

    @Override
    public void addReferral(ReferralRQ referralRQ) throws TreatmentNotFoundException {
        Optional<Treatment> optionalTreatment = treatmentRepository.findTreatmentByAppointmentId(referralRQ.getTreatmentId());
        if (optionalTreatment.isEmpty()) throw new TreatmentNotFoundException();
        Treatment treatment = optionalTreatment.get();
        Referral referral = new Referral();
        referral.setTreatment(treatment);
        referral.setTestType(TestTypeMapper.toEntity(referralRQ.getTestType()));
        referral.setExpirationDate(Timestamp.valueOf(referralRQ.getExpirationDate()));
        referralRepository.save(referral);
        treatment.setReferral(referral);
        treatmentRepository.save(treatment);
    }
}
