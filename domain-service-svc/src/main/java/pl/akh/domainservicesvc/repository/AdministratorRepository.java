package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Administrator;

import java.util.UUID;

public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {
}
