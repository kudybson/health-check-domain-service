package pl.akh.model.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TreatmentRQ {
    @NotNull
    private Long appointmentId;
    @NotBlank
    private String diagnosis;
    @NotBlank
    private String recommendation;

}
