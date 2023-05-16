package pl.akh.model.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentRQ {
    @NotNull
    private Long appointmentId;
    @NotBlank
    private String diagnosis;
    @NotBlank
    private String recommendation;
}
