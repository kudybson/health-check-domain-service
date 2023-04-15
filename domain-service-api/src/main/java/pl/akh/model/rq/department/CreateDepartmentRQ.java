package pl.akh.model.rq.department;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import pl.akh.model.rq.AddressRQ;
import pl.akh.utils.AddressConstraint;

@Data
@Builder
public class CreateDepartmentRQ {
    @NotBlank
    String name;
    @AddressConstraint
    private AddressRQ addressRQ;
}
