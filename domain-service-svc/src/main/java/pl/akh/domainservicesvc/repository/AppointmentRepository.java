package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    //TODO tests after adding new column with room number
}
