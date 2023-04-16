package pl.akh.services;

import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestRQ;
import pl.akh.model.rs.MedicalTestRS;

import java.util.Collection;
import java.util.UUID;

public interface MedicalTestService {
    Collection<MedicalTestRS> getMedicalTestsByTypeAndDepartmentId(TestType testType, long departmentId);

    Collection<MedicalTestRS> getMedicalTestsByDepartmentId(long departmentId);

    MedicalTestRS getMedicalTestById(long id);

    MedicalTestRS createMedicalTest(MedicalTestRQ medicalTestRQ);

    Collection<MedicalTestRS> getAllMedicalByPatientId(UUID id);


}
