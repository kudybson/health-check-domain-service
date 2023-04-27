package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.akh.domainservicesvc.domain.model.entities.Schedule;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> getSchedulesByDoctorIdAndStartDateTimeAfterAndEndDateTimeBefore(UUID uuid, Timestamp startDate, Timestamp endDate);

    @Query("select s from Schedule s where s.doctor.id= :doctorId and s.startDateTime <= :startDateTime and s.endDateTime >= :endDateTime")
    Optional<Schedule> getScheduleByDoctorIdAndStartDateTimeAndEndDateTime(@Param("doctorId") UUID doctorId, @Param("startDateTime") Timestamp startDateTime,
                                                                           @Param("endDateTime") Timestamp endDateTime);
}
