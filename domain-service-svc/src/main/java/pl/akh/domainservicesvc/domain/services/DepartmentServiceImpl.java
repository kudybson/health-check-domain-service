package pl.akh.domainservicesvc.domain.services;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.akh.domainservicesvc.domain.mappers.DepartmentMapper;
import pl.akh.domainservicesvc.domain.model.entities.Address;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.utils.validation.ValidationUtils;
import pl.akh.model.rq.AddressRQ;
import pl.akh.model.rq.DepartmentRQ;
import pl.akh.model.rs.DepartmentRS;
import pl.akh.services.DepartmentService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final Validator validator;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, Validator validator) {
        this.departmentRepository = departmentRepository;
        this.validator = validator;
    }

    @Override
    public DepartmentRS createDepartment(@NotNull DepartmentRQ departmentRQ) {
        ValidationUtils.validateEntity(departmentRQ, validator);

        log.info("Saving new department.");
        Address address = new Address();
        setupAddress(address, departmentRQ.getAddressRQ());
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
                .ifPresent(address -> setupAddress(address, departmentRQ.getAddressRQ()));
        department.setName(departmentRQ.getName());
        return DepartmentMapper.mapToDto(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    private void setupAddress(Address address, AddressRQ addressRQ) {
        address.setProvince(addressRQ.getProvince());
        address.setCountry(addressRQ.getCountry());
        address.setPost(addressRQ.getPost());
        address.setStreet(addressRQ.getStreet());
        address.setPostalCode(addressRQ.getPostalCode());
        address.setHouseNumber(addressRQ.getHouseNumber());
        address.setApartmentNumber(addressRQ.getApartmentNumber());
        address.setCounty(addressRQ.getCounty());
        address.setCity(addressRQ.getCity());
    }
}
