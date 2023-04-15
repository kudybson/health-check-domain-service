package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
