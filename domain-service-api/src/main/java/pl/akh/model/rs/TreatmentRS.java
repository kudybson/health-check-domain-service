package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TreatmentRS {
    private Long id;
    long appointmentId;
    long referralId;
    private String diagnosis;
    private String recommendation;
    PrescriptionRS prescription;

}
