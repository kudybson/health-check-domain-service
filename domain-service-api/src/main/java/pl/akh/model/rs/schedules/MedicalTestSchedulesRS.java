package pl.akh.model.rs.schedules;

import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.TestType;

import java.util.Collection;

@Data
@Builder
public class MedicalTestSchedulesRS {
    private Long departmentId;
    private TestType type;
    private Collection<ScheduleRS> receptionSchedules;
    private Collection<ScheduleRS> assignedSchedules;
}
