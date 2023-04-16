package pl.akh.services;

import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestScheduleRQ;
import pl.akh.model.rs.schedules.MedicalTestSchedulesRS;

public interface MedicalTestScheduleService {

    MedicalTestSchedulesRS insertMedicalTestSchedules(MedicalTestScheduleRQ medicalTestScheduleRQ);

    MedicalTestSchedulesRS getMedicalTestSchedules(Long departmentId, TestType testType);
}
