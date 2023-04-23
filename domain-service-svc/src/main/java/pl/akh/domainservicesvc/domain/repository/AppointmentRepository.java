package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.Appointment;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Duration APPOINTMENT_TIME = Duration.ofMinutes(15L);

    List<Appointment> getAppointmentsByDoctorIdAndAppointmentDateIsBetween(UUID id, Timestamp startDate, Timestamp endDate);
}
