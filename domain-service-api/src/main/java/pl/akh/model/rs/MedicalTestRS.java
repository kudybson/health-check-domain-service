package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.TestStatus;
import pl.akh.model.common.TestType;

import java.util.UUID;

@Data
@Builder
public class MedicalTestRS {

    private Long id;
    private TestStatus testStatus;
    long departmentId;
    UUID patientUUID;
    TestType type;
    String description;
    TestResultRS testResultRS;
}
