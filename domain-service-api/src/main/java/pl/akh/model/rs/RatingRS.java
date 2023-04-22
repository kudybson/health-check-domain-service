package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RatingRS {
    private Long id;
    private UUID patientUUID;
    private UUID doctorUUID;
    private Long grade;
    private String description;
}
