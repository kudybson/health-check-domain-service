package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTest;
import pl.akh.domainservicesvc.domain.model.entities.enums.TestType;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicalTestRepository extends JpaRepository<MedicalTest, Long> {
    Duration MEDICAL_TEST_DURATION = Duration.ofMinutes(15L);

    @Query(value = "select mt.testDate from MedicalTest mt " +
            "where mt.department.id=:departmentId and mt.type=:testType and mt.testDate>=:start and mt.testDate<=:end")
    List<Timestamp> findScheduleDatesBetween(Long departmentId, TestType testType, Timestamp start, Timestamp end);


    @Query(value = "select mt from MedicalTest mt join fetch mt.department join fetch mt.patient " +
            "where mt.department.id=:departmentId and mt.type=:testType and mt.testDate>=:start and mt.testDate<=:end")
    List<MedicalTest> findAllByDepartmentIdAndType(Long departmentId, TestType testType, Timestamp start, Timestamp end);

    @Query(value = "select mt from MedicalTest mt join fetch mt.department join fetch mt.patient " +
            "where mt.patient.id=:id")
    Collection<MedicalTest> findMedicalTestsByPatientUUID(UUID id);

    @Query(value = "select mt from MedicalTest mt " +
            "where mt.department.id=:departmentId and mt.type=:type and mt.testDate=:timestamp")
    Optional<MedicalTest> findMedicalTestByDepartmentIdAndTypeAndTestDateEquals(Long departmentId, TestType type, Timestamp timestamp);

    @Query(value = "select mt from MedicalTest mt join fetch mt.department join fetch mt.patient " +
            "where mt.department.id=:departmentId and mt.testDate>=:start and mt.testDate<=:end")
    Collection<MedicalTest> findAllByDepartmentId(Long departmentId, Timestamp start, Timestamp end);

}
