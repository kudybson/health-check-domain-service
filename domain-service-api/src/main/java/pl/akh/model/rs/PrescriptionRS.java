package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class PrescriptionRS {
    private Long id;
    private TreatmentRS treatmentRS;
    private String code;
    private String description;
    private Timestamp expirationDate;
}
