package pl.akh.model.rq;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.akh.utils.AddressConstraint;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRQ {
    @NotBlank
    String name;
    @AddressConstraint
    @NotNull

    private AddressRQ addressRQ;
}
