package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.MedicalTestSchedule;
import pl.akh.model.rs.schedules.ScheduleRS;

public class MedicalTestScheduleMapper {
    public static ScheduleRS toScheduleRS(MedicalTestSchedule medicalTestSchedule) {
        if (medicalTestSchedule == null) return null;
        return ScheduleRS.builder()
                .id(medicalTestSchedule.getId())
                .startDateTime(medicalTestSchedule.getStartDateTime().toLocalDateTime())
                .endDateTime(medicalTestSchedule.getEndDateTime().toLocalDateTime())
                .build();
    }
}
