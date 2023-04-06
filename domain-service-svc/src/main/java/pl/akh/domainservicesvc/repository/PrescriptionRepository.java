package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
