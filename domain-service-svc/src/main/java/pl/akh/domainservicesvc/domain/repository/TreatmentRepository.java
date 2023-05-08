package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.domain.model.entities.Treatment;

import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    @Query(value = "SELECT t from Treatment t where t.appointment.id= :appointmentId")
    Optional<Treatment> findTreatmentByAppointmentId(Long appointmentId);

    @Override
    void deleteById(Long id);
}
