package pl.akh.domainservicesvc.domain.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class AccessService {
    DepartmentRepository departmentRepository;

    @Autowired
    public AccessService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public boolean hasAccessAdministrationAccessToDepartment(UUID uuid, Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow();
        if (department.getAdministrator() != null) {
            return Objects.equals(department.getAdministrator().getId(), uuid);
        }
        return false;
    }
}
