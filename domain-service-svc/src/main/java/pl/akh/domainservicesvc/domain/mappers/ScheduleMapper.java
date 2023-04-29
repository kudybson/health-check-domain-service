package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Schedule;
import pl.akh.model.rs.schedules.ScheduleRS;

public class ScheduleMapper {
    public static ScheduleRS mapToDto(Schedule schedule) {
        if (schedule == null) return null;
        return ScheduleRS.builder()
                .startDateTime(schedule.getStartDateTime().toLocalDateTime())
                .endDateTime(schedule.getEndDateTime().toLocalDateTime())
                .build();
    }
}
