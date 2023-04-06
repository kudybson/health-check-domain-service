package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
