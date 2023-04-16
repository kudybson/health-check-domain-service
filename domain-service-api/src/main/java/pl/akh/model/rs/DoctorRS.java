package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.Specialization;

import java.util.UUID;

@Data
@Builder
public class DoctorRS {
    private UUID doctorUUID;
    private String firstname;
    private String lastname;
    private Specialization specialization;
    private Long departmentId;
}
