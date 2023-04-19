package pl.akh.model.rq;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.akh.model.common.TestType;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalTestScheduleRQ {
    @NotNull
    private Long departmentId;
    @NotNull
    private TestType testType;
    @NotEmpty
    private Collection<ScheduleRQ> schedules;
}
