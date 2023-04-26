package pl.akh.domainservicesvc.domain.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.Receptionist;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.repository.ReceptionistRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class AccessService {
    DepartmentRepository departmentRepository;
    ReceptionistRepository receptionistRepository;

    @Autowired
    public AccessService(DepartmentRepository departmentRepository, ReceptionistRepository receptionistRepository) {
        this.departmentRepository = departmentRepository;
        this.receptionistRepository = receptionistRepository;
    }

    public boolean hasAccessAdministrationAccessToDepartment(UUID uuid, Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow();
        if (department.getAdministrator() != null) {
            return Objects.equals(department.getAdministrator().getId(), uuid);
        }
        return false;
    }

    public boolean hasReceptionistAccessToDepartment(UUID id, Long departmentId) {
        return receptionistRepository.findById(id)
                .map(Receptionist::getDepartment)
                .map(Department::getId)
                .filter(depIdFromDB -> Objects.equals(depIdFromDB, departmentId)).isPresent();
    }
}
