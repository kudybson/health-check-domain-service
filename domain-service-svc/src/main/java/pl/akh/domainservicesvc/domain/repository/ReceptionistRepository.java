package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.Receptionist;

import java.util.List;
import java.util.UUID;

public interface ReceptionistRepository extends JpaRepository<Receptionist, UUID> {
    List<Receptionist> findAllByDepartmentId(Long departmentId);

    @Query(value = "SELECT r.department FROM RECEPTIONIST r where r.id= :receptionistId")
    Department findDepartmentByReceptionistId(UUID receptionistId);
}
