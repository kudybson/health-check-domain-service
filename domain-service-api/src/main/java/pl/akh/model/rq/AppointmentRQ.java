package pl.akh.model.rq;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRQ {
    @NotNull
    @org.hibernate.validator.constraints.UUID
    private UUID doctorUUID;
    @NotNull
    @org.hibernate.validator.constraints.UUID
    private UUID patientUUID;
    @NotNull
    private LocalDateTime appointmentDateTime;

}
