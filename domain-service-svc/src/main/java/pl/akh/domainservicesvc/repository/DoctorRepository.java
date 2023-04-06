package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Doctor;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
}
