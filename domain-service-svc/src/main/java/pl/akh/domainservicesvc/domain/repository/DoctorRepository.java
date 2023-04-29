package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.domain.model.entities.Doctor;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID>, JpaSpecificationExecutor<Doctor> {

    @Query("select d.department.id from Doctor d where d.id = :id")
    Long getDepartmentIdByDoctorId(UUID id);
}
