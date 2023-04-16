package pl.akh.model.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PrescriptionRQ {
    @NotNull
    private Long treatmentId;
    @NotBlank
    private String code;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate expirationDate;
}
