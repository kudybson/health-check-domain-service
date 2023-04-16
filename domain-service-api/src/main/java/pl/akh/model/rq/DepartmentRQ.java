package pl.akh.model.rq;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import pl.akh.utils.AddressConstraint;

@Data
@Builder
public class DepartmentRQ {
    @NotBlank
    String name;
    @AddressConstraint
    private AddressRQ addressRQ;
}
