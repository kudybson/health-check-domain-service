package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.MedicalTest;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTestResult;
import pl.akh.model.common.TestStatus;
import pl.akh.model.common.TestType;
import pl.akh.model.rs.MedicalTestRS;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

public class MedicalTestMapper {
    public static MedicalTestRS toDTO(MedicalTest entity) {
        if (entity == null) return null;
        TestStatus status = TestStatus.DONE;
        if (entity.getMedicalTestResult() == null) {
            status = TestStatus.WAITING_FOR_RESULT;
        }
        if (entity.getTestDate().before(Timestamp.valueOf(LocalDateTime.now()))) {
            status = TestStatus.NOT_PERFORMED;
        }
        return MedicalTestRS.builder()
                .id(entity.getId())
                .departmentId(entity.getDepartment().getId())
                .type(TestType.valueOf(entity.getType().name()))
                .medicalTestResultId(
                        ofNullable(entity.getMedicalTestResult()).map(MedicalTestResult::getId).orElse(null))
                .testStatus(status)
                .patientUUID(entity.getPatient().getId())
                .build();
    }
}
