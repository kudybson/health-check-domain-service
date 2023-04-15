package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.Receptionist;

import java.util.UUID;

public interface ReceptionistRepository extends JpaRepository<Receptionist, UUID> {
}
