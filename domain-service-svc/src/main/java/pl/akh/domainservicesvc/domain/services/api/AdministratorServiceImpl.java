package pl.akh.domainservicesvc.domain.services.api;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PasswordConfirmationException;
import pl.akh.domainservicesvc.domain.mappers.AdministratorMapper;
import pl.akh.domainservicesvc.domain.model.entities.Administrator;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.repository.AdministratorRepository;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.services.StuffServiceImpl;
import pl.akh.model.rq.AdministratorRQ;
import pl.akh.model.rs.AdministratorRS;
import pl.akh.services.AdministratorService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class AdministratorServiceImpl implements AdministratorService {

    private final StuffServiceImpl stuffService;

    private final DepartmentRepository departmentRepository;
    private final AdministratorRepository administratorRepository;

    @Autowired
    public AdministratorServiceImpl(StuffServiceImpl stuffService, DepartmentRepository departmentRepository, AdministratorRepository administratorRepository) {
        this.stuffService = stuffService;
        this.departmentRepository = departmentRepository;
        this.administratorRepository = administratorRepository;
    }


    @Override
    public AdministratorRS addAdministrator(AdministratorRQ administratorRQ) throws Exception {
        if (!Objects.equals(administratorRQ.getPassword(), administratorRQ.getPasswordConfirmation())) {
            throw new PasswordConfirmationException("Passwords are not the same.");
        }
        Department department = departmentRepository.findById(administratorRQ.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(String.format("Department with id: %d not found.", administratorRQ.getDepartmentId())));
        if (department.getAdministrator() != null) {
            throw new UnsupportedOperationException("Admin is already assigned to this department.");
        }
        UUID administratorUUID = stuffService.addStuffMember(administratorRQ);

        Administrator administrator = new Administrator();
        administrator.setDepartment(department);
        administrator.setId(administratorUUID);
        administrator.setFirstName(administratorRQ.getFirstName());
        administrator.setLastName(administratorRQ.getLastName());
        administratorRepository.save(administrator);
        return AdministratorMapper.mapToDto(administrator);
    }

    @Override
    public Optional<AdministratorRS> getAdministratorById(UUID administratorUUID) throws Exception {
        return administratorRepository.findById(administratorUUID).map(AdministratorMapper::mapToDto);
    }

    @Override
    public void deleteAdministrator(UUID administratorUUID) throws Exception {
        administratorRepository.deleteById(administratorUUID);
    }

    @Override
    public Optional<AdministratorRS> getAdministratorByDepartmentId(Long departmentId) throws Exception {
        return departmentRepository.findById(departmentId)
                .map(Department::getAdministrator)
                .map(AdministratorMapper::mapToDto);
    }
}
