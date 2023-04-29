package pl.akh.services;

import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestRQ;
import pl.akh.model.rs.MedicalTestRS;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface MedicalTestService {

    Collection<MedicalTestRS> getMedicalTestsByTypeAndDepartmentId(TestType testType, Long departmentId, LocalDateTime start, LocalDateTime end);

    Collection<MedicalTestRS> getMedicalTestsByDepartmentId(Long departmentId, LocalDateTime start, LocalDateTime end);

    Optional<MedicalTestRS> getMedicalTestById(Long id);

    MedicalTestRS createMedicalTest(MedicalTestRQ medicalTestRQ) throws Exception;

    Collection<MedicalTestRS> getAllMedicalByPatientId(UUID id);

    void cancelMedicalTest(Long testId);
}
