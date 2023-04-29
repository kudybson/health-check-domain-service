package pl.akh.services;

import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestScheduleRQ;
import pl.akh.model.rs.schedules.MedicalTestSchedulesRS;
import pl.akh.model.rs.schedules.ScheduleRS;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MedicalTestScheduleService {

    Collection<ScheduleRS> insertMedicalTestSchedules(MedicalTestScheduleRQ medicalTestScheduleRQ);


    MedicalTestSchedulesRS getMedicalTestSchedules(Long departmentId, TestType testType, LocalDateTime startDate, LocalDateTime endDate);
}
