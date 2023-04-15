package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
}
