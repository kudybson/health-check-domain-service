package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TreatmentRS {
    private Long id;
    Long appointmentId;
    Long referralId;
    private String diagnosis;
    private String recommendation;
    Long prescriptionId;
}
