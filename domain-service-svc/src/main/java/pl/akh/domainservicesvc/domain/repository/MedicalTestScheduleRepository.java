package pl.akh.domainservicesvc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTestSchedule;
import pl.akh.domainservicesvc.domain.model.entities.TestType;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MedicalTestScheduleRepository extends JpaRepository<MedicalTestSchedule, Long> {

    @Query(value = "select mts from MedicalTestSchedule mts where mts.department.id=:depId and mts.type=:type " +
            "and mts.startDateTime<=:endDate and mts.endDateTime >=:startDate")
    Collection<MedicalTestSchedule> findByDepartmentAndTypeBetweenDates(Long depId, TestType type, Timestamp startDate, Timestamp endDate);

    @Query(value = "select mts from MedicalTestSchedule mts where mts.department.id=:depId and mts.type=:type " +
            "and mts.startDateTime<=:startDate and mts.endDateTime >=:endDate")
    Optional<MedicalTestSchedule> findScheduleWhichContainPeriod(Long depId, TestType type, Timestamp startDate, Timestamp endDate);
}
