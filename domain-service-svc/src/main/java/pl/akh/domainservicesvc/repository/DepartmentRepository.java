package pl.akh.domainservicesvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.akh.domainservicesvc.model.entities.Department;


public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
