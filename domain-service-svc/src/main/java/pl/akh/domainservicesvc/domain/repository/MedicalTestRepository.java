package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTest;

public interface MedicalTestRepository extends JpaRepository<MedicalTest, Long> {
}
