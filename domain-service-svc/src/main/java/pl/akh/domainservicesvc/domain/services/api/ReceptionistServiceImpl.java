package pl.akh.domainservicesvc.domain.services.api;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PasswordConfirmationException;
import pl.akh.domainservicesvc.domain.mappers.ReceptionistMapper;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.Receptionist;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.repository.ReceptionistRepository;
import pl.akh.domainservicesvc.domain.services.StuffServiceImpl;
import pl.akh.model.rq.ReceptionistRQ;
import pl.akh.model.rs.ReceptionistRS;
import pl.akh.services.ReceptionistService;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class ReceptionistServiceImpl implements ReceptionistService {

    private final StuffServiceImpl stuffService;
    private final DepartmentRepository departmentRepository;
    private final ReceptionistRepository receptionistRepository;

    @Autowired
    public ReceptionistServiceImpl(StuffServiceImpl stuffService, DepartmentRepository departmentRepository, ReceptionistRepository receptionistRepository) {
        this.stuffService = stuffService;
        this.departmentRepository = departmentRepository;
        this.receptionistRepository = receptionistRepository;
    }

    @Override
    public ReceptionistRS createReceptionist(ReceptionistRQ receptionistRQ) throws Exception {
        if (!Objects.equals(receptionistRQ.getPassword(), receptionistRQ.getPasswordConfirmation())) {
            throw new PasswordConfirmationException("Passwords are not the same.");
        }
        Department department = departmentRepository.findById(receptionistRQ.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(String.format("Department with id: %d not found.", receptionistRQ.getDepartmentId())));

        UUID receptionistUUID = stuffService.addStuffMember(receptionistRQ);

        Receptionist receptionist = new Receptionist();
        receptionist.setDepartment(department);
        receptionist.setId(receptionistUUID);
        receptionist.setFirstName(receptionistRQ.getFirstName());
        receptionist.setLastName(receptionistRQ.getLastName());
        Receptionist saved = receptionistRepository.save(receptionist);
        return ReceptionistMapper.mapToDto(saved);
    }

    @Override
    public Optional<ReceptionistRS> getReceptionistByUUID(UUID receptionistUUID) throws Exception {
        return receptionistRepository.findById(receptionistUUID).map(ReceptionistMapper::mapToDto);
    }

    @Override
    public void deleteReceptionist(UUID receptionistUUID) throws Exception {
        Optional<Receptionist> receptionist = receptionistRepository.findById(receptionistUUID);
        receptionist.ifPresent(receptionistRepository::delete);
    }

    @Override
    public Collection<ReceptionistRS> getReceptionistsByDepartmentId(Long departmentId) {
        return receptionistRepository.findAllByDepartmentId(departmentId)
                .stream()
                .map(ReceptionistMapper::mapToDto)
                .toList();
    }
}
