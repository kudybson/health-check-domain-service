package pl.akh.model.rs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentRS {
    private Long id;
    private String name;
    private AddressRS address;
}
