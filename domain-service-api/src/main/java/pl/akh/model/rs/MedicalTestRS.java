package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.TestStatus;
import pl.akh.model.common.TestType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MedicalTestRS {
    private Long id;
    private TestStatus testStatus;
    private Long departmentId;
    private UUID patientUUID;
    private TestType type;
    private Long medicalTestResultId;
    private LocalDateTime testDateTime;
}
