package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
}
