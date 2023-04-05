package pl.akh.model.rq.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AddAdminToDepartmentRQ {
    @org.hibernate.validator.constraints.UUID
    private UUID adminUUID;

    @NotNull
    private Long departmentId;
}
