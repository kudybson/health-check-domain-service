package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Patient;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
}
