package pl.akh.model.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
