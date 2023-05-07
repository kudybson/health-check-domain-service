package pl.akh.model.rq;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateRQ {
    @NotNull
    private UUID doctorUUID;
    @NotNull
    private UUID patientUUID;
    @Min(value = 0)
    @Max(value = 10)
    @NotNull
    private Long grade;
    private String description;
}
