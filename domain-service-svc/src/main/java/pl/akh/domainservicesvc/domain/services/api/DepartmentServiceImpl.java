package pl.akh.domainservicesvc.domain.services.api;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.exceptions.AdministratorNotFoundException;
import pl.akh.domainservicesvc.domain.mappers.AddressMapper;
import pl.akh.domainservicesvc.domain.mappers.DepartmentMapper;
import pl.akh.domainservicesvc.domain.model.entities.Address;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.repository.AdministratorRepository;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.utils.validation.ValidationUtils;
import pl.akh.model.rq.DepartmentRQ;
import pl.akh.model.rs.DepartmentRS;
import pl.akh.services.DepartmentService;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final AdministratorRepository administratorRepository;
    private final Validator validator;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, AdministratorRepository administratorRepository, Validator validator) {
        this.departmentRepository = departmentRepository;
        this.administratorRepository = administratorRepository;
        this.validator = validator;
    }

    @Override
    public DepartmentRS createDepartment(@NotNull DepartmentRQ departmentRQ) {
        ValidationUtils.validateEntity(departmentRQ, validator);

        log.info("Saving new department.");
        Address address = AddressMapper.mapToEntity(departmentRQ.getAddressRQ());
        Department department = new Department();
        department.setName(departmentRQ.getName());
        department.setAddress(address);
        Department savedDepartment = departmentRepository.save(department);
        log.info("Department saved with id: {}", savedDepartment.getId());
        return DepartmentMapper.mapToDto(savedDepartment);
    }

    @Override
    public Collection<DepartmentRS> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<DepartmentRS> getDepartmentsByCriteria(String city, String street, String county, String province, String country) {
        throw new RuntimeException();
    }

    @Override
    public DepartmentRS getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(DepartmentMapper::mapToDto)
                .get();
    }

    @Override
    public DepartmentRS updateDepartment(DepartmentRQ departmentRQ, Long departmentId) {
        ValidationUtils.validateEntity(departmentRQ, validator);
        Department department = departmentRepository.findById(departmentId).orElseThrow();
        Optional.of(department.getAddress())
                .ifPresent(address -> AddressMapper.mapToEntity(address, departmentRQ.getAddressRQ()));
        department.setName(departmentRQ.getName());
        return DepartmentMapper.mapToDto(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentRS getDepartmentByAdministratorId(UUID administratorId) throws AdministratorNotFoundException {
        if(!administratorRepository.existsById(administratorId)) throw new AdministratorNotFoundException();
        return DepartmentMapper.mapToDto(departmentRepository.findDepartmentByAdministratorId(administratorId));
    }
}
