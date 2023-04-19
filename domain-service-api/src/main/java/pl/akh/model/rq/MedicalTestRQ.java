package pl.akh.model.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.akh.model.common.TestType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalTestRQ {
    private Long departmentId;
    private UUID patientUUID;
    private TestType type;
    private LocalDateTime testDate;
}
