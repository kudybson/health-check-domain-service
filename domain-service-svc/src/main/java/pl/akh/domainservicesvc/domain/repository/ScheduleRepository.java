package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.Schedule;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> getSchedulesByDoctorIdAndStartDateTimeAfterAndEndDateTimeBefore(UUID uuid, Timestamp startDate, Timestamp endDate);
}
