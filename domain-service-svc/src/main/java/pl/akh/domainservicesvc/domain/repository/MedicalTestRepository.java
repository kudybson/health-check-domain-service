package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTest;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface MedicalTestRepository extends JpaRepository<MedicalTest, Long> {
    Duration MEDICAL_TEST_DURATION = Duration.ofMinutes(15L);

    @Query(value = "select mt.testDate from MedicalTest mt where mt.testDate>=:start and mt.testDate<=:end")
    List<Timestamp> findScheduleDatesBetween(Timestamp start, Timestamp end);
}
