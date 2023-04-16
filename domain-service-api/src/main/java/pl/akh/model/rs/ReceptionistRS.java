package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReceptionistRS {
    private UUID receptionistUUID;
    private String firstname;
    private String lastname;
    private Long departmentId;
}
