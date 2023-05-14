package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.MedicalTest;
import pl.akh.domainservicesvc.domain.model.entities.enums.TestResultStatus;
import pl.akh.model.common.TestStatus;
import pl.akh.model.common.TestType;
import pl.akh.model.rs.MedicalTestRS;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MedicalTestMapper {
    public static MedicalTestRS toDTO(MedicalTest entity) {
        if (entity == null) return null;
        TestStatus status = TestStatus.DONE;
        if (entity.getResultReady() == TestResultStatus.WAITING_FOR_RESULT) {
            status = TestStatus.WAITING_FOR_RESULT;
        }
        if (entity.getTestDate().after(Timestamp.valueOf(LocalDateTime.now()))) {
            status = TestStatus.NOT_PERFORMED;
        }
        return MedicalTestRS.builder()
                .id(entity.getId())
                .departmentId(entity.getDepartment().getId())
                .departmentName(entity.getDepartment().getName())
                .type(TestType.valueOf(entity.getType().name()))
                .testStatus(status)
                .patientUUID(entity.getPatient().getId())
                .testDateTime(entity.getTestDate().toLocalDateTime())
                .build();
    }

    public static MedicalTestRS toDTOWithPatient(MedicalTest entity) {
        MedicalTestRS medicalTestRS = MedicalTestMapper.toDTO(entity);
        medicalTestRS.setPatient(PatientMapper.mapToDto(entity.getPatient()));
        return medicalTestRS;
    }
}
