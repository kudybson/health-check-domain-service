package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.domain.model.entities.Department;

import java.util.Optional;


public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query(value = "FROM Department dep join fetch dep.doctors doc where dep.id=:id")
    Optional<Department> findDepartmentByIdWithDoctors(Long id);
}
