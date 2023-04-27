package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.domain.model.entities.Appointment;
import pl.akh.domainservicesvc.domain.model.entities.Status;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Duration APPOINTMENT_TIME = Duration.ofMinutes(15L);

    List<Appointment> getAppointmentsByDoctorIdAndAppointmentDateIsBetween(UUID id, Timestamp startDate, Timestamp endDate);

    @Query("select a from Appointment a where a.doctor.id = :id and a.appointmentDate = :appointmentDate and a.status = :status")
    Optional<Appointment> findAppointmentByDoctorIdAndAppointmentDate(UUID id, Timestamp appointmentDate, Status status);
}
