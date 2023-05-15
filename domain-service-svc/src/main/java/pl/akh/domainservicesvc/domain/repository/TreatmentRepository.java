package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.Treatment;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    void deleteByAppointmentId(long appointmentId);
}
