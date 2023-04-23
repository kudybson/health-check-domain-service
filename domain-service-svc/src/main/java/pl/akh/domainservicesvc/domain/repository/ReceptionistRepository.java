package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.domain.model.entities.Receptionist;

import java.util.List;
import java.util.UUID;

public interface ReceptionistRepository extends JpaRepository<Receptionist, UUID> {
    List<Receptionist> findAllByDepartmentId(Long departmentId);
}
