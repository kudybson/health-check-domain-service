package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    //TODO tests after adding new column with room number
}
