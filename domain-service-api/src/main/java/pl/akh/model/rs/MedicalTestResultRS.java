package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicalTestResultRS {
    private Long id;
    private String description;

}
