package pl.akh.model.rq;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.TestType;

import java.time.LocalDateTime;

@Data
@Builder
public class ReferralRQ {
    @NotNull
    private Long treatmentId;
    @NotNull
    private TestType testType;
    @NotNull
    private LocalDateTime expirationDate;
}
