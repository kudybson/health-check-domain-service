package pl.akh.model.rq.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RateRQ {
    @Min(value = 0)
    @Max(value = 10)
    @NotNull
    private Integer grade;
}
