package pl.akh.services;

import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestScheduleRQ;
import pl.akh.model.rs.schedules.MedicalTestSchedules;

public interface MedicalTestScheduleService {

    MedicalTestSchedules insertMedicalTestSchedules(MedicalTestScheduleRQ medicalTestScheduleRQ);

    MedicalTestSchedules getMedicalTestSchedules(Long departmentId, TestType testType);
}
