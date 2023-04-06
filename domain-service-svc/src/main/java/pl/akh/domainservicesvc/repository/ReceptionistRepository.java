package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Receptionist;

import java.util.UUID;

public interface ReceptionistRepository extends JpaRepository<Receptionist, UUID> {
}
