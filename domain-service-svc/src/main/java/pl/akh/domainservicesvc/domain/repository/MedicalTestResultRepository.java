package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTestResult;

public interface MedicalTestResultRepository extends JpaRepository<MedicalTestResult, Long> {
}
