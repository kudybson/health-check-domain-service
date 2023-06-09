package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.Specialization;

import java.util.UUID;

@Data
@Builder
public class AdministratorRS {
    private UUID administratorUUID;
    private String firstname;
    private String lastname;
    private Long departmentId;
}
