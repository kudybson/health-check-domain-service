package pl.akh.model.rq;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.akh.model.common.TestType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferralRQ {
    @NotNull
    private Long treatmentId;
    @NotNull
    private TestType testType;
    @NotNull
    private LocalDateTime expirationDate;
}
