package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Treatment;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
}
