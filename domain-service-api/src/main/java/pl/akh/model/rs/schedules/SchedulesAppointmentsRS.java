package pl.akh.model.rs.schedules;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class SchedulesAppointmentsRS {
    private Collection<ScheduleRS> schedules;
    private Collection<AppointmentDateRS> appointments;
}
