package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.TestSchedule;

public interface TestScheduleRepository extends JpaRepository<TestSchedule, Long> {
}
