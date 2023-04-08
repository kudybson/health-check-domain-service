package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.model.entities.Department;

import java.util.Optional;
import java.util.UUID;


public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query(value = "FROM Department dep join fetch dep.doctors doc where dep.id=:id")
    Optional<Department> findDepartmentByIdWithDoctors(Long id);
}
