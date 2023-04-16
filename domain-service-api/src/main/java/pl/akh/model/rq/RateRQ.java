package pl.akh.model.rq;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RateRQ {
    @NotNull
    @org.hibernate.validator.constraints.UUID
    private UUID doctorUUID;
    @NotNull
    @org.hibernate.validator.constraints.UUID
    private UUID patientUUID;
    @Min(value = 0)
    @Max(value = 10)
    @NotNull
    private Integer grade;
}