package pl.akh.domainservicesvc.domain.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PasswordConfirmationException;
import pl.akh.domainservicesvc.domain.mappers.DoctorMapper;
import pl.akh.domainservicesvc.domain.mappers.RatingMapper;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.repository.DoctorRepository;
import pl.akh.model.common.Specialization;
import pl.akh.model.rq.DoctorRQ;
import pl.akh.model.rs.DoctorRS;
import pl.akh.model.rs.RatingRS;
import pl.akh.services.DoctorService;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static pl.akh.domainservicesvc.domain.mappers.SpecializationMapper.toEntity;

@Service
@Slf4j
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final StuffServiceImpl stuffService;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(StuffServiceImpl stuffService, DepartmentRepository departmentRepository, DoctorRepository doctorRepository) {
        this.stuffService = stuffService;
        this.departmentRepository = departmentRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorRS createDoctor(DoctorRQ doctorRQ) throws Exception {
        if (!Objects.equals(doctorRQ.getPassword(), doctorRQ.getPasswordConfirmation())) {
            throw new PasswordConfirmationException("Passwords are not the same.");
        }
        Department department = departmentRepository.findById(doctorRQ.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(String.format("Department with id: %d not found.", doctorRQ.getDepartmentId())));

        UUID doctorUUID = stuffService.addStuffMember(doctorRQ);

        Doctor doctor = new Doctor();
        doctor.setId(doctorUUID);
        doctor.setFirstName(doctorRQ.getFirstName());
        doctor.setLastName(doctorRQ.getLastName());
        doctor.setSpecialization(toEntity(doctorRQ.getSpecialization()));
        doctor.setDepartment(department);
        Doctor save = doctorRepository.save(doctor);
        return DoctorMapper.mapToDto(save);
    }

    @Override
    public Optional<DoctorRS> getDoctorById(UUID doctorUUID) {
        return doctorRepository.findById(doctorUUID)
                .map(DoctorMapper::mapToDto);
    }

    @Override
    public Collection<DoctorRS> getDoctorsByCriteria(Specialization specialization, Long departmentId, String firstname, String lastName) {
        return doctorRepository.findAll(createSearchCriteria(specialization, departmentId, firstname, lastName))
                .stream()
                .map(DoctorMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<RatingRS> getDoctorRates(UUID doctorUUID) throws DoctorNotFoundException {
        Doctor doctor = doctorRepository.findById(doctorUUID).orElseThrow(DoctorNotFoundException::new);
        return doctor.getRatings()
                .stream()
                .map(RatingMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDoctor(UUID doctorUUID) {
        doctorRepository.deleteById(doctorUUID);
    }

    private Specification<Doctor> createSearchCriteria(Specialization specialization, Long departmentId, String firstname, String lastName) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            ofNullable(specialization).ifPresent((spec) -> predicates.add(criteriaBuilder.equal(root.get("specialization"), spec)));
            ofNullable(departmentId).ifPresent((depId) -> predicates.add(criteriaBuilder.equal(root.get("department").get("id"), depId)));
            ofNullable(firstname).ifPresent((fName) -> predicates.add(criteriaBuilder.like(root.get("firstName"), fName)));
            ofNullable(lastName).ifPresent((lName) -> predicates.add(criteriaBuilder.equal(root.get("lastName"), lName)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
