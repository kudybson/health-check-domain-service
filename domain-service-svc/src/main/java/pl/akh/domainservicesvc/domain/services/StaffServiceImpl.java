package pl.akh.domainservicesvc.domain.services;

import jakarta.servlet.UnavailableException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFountException;
import pl.akh.domainservicesvc.domain.mappers.EntityToDtoMapper;
import pl.akh.domainservicesvc.domain.mappers.MapperFactory;
import pl.akh.domainservicesvc.domain.model.entities.Administrator;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.Receptionist;
import pl.akh.domainservicesvc.domain.repository.AdministratorRepository;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.repository.ReceptionistRepository;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.Groups;
import pl.akh.model.rq.AdministratorRQ;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.CreateOauth2User;
import pl.akh.model.rq.DoctorRQ;
import pl.akh.model.rq.ReceptionistRQ;
import pl.akh.model.rs.AdministratorRS;
import pl.akh.model.rs.DoctorRS;
import pl.akh.model.rs.ReceptionistRS;
import pl.akh.services.StaffService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class StaffServiceImpl implements StaffService {
    private final AdministratorRepository administratorRepository;
    private final DepartmentRepository departmentRepository;
    private final ReceptionistRepository receptionistRepository;
    private final OAuth2Service oAuth2Service;
    private EntityToDtoMapper<Administrator, AdministratorRS> administratorMapper;
    private EntityToDtoMapper<Receptionist, ReceptionistRS> receptionistMapper;

    public StaffServiceImpl(AdministratorRepository administratorRepository, DepartmentRepository departmentRepository, ReceptionistRepository receptionistRepository, OAuth2Service oAuth2Service) {
        this.administratorRepository = administratorRepository;
        this.departmentRepository = departmentRepository;
        this.receptionistRepository = receptionistRepository;
        this.oAuth2Service = oAuth2Service;
        administratorMapper = MapperFactory.getEntityToDtoMapper(Administrator.class, AdministratorRS.class);
        receptionistMapper = MapperFactory.getEntityToDtoMapper(Receptionist.class, ReceptionistRS.class);
    }

    @Override
    public AdministratorRS addAdministrator(AdministratorRQ administratorRQ) throws Exception {
        if (!Objects.equals(administratorRQ.getPassword(), administratorRQ.getPasswordConfirmation())) {
            throw new UnsupportedOperationException();
        }
        Department department = departmentRepository.findById(administratorRQ.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFountException(String.format("Department with id: %d not found.", administratorRQ.getDepartmentId())));

        UUID administratorUUID = createUserInAuthorizationService(administratorRQ);
        Administrator administrator = new Administrator();
        administrator.setDepartment(department);
        administrator.setId(administratorUUID);
        administrator.setFirstName(administratorRQ.getFirstName());
        administrator.setSecondName(administratorRQ.getLastName());
        return administratorMapper.mapToDto(administrator);
    }

    @Override
    public ReceptionistRS createReceptionist(ReceptionistRQ receptionistRQ) {
        return null;
    }

    @Override
    public DoctorRS createDoctor(DoctorRQ doctorRQ) {
        return null;
    }

    @Override
    public void deleteStaffMember(UUID staffMemberUUID) {

    }

    @Override
    public void lockStaffMemberAccount(UUID receptionistUUID) {

    }

    @Override
    public void unlockStaffMemberAccount(UUID receptionistUUID) {

    }

    private UUID createUserInAuthorizationService(AdministratorRQ administratorRQ) throws UnavailableException {
        CreateOauth2User build = CreateOauth2User.builder()
                .username(administratorRQ.getUsername())
                .firstName(administratorRQ.getFirstName())
                .lastName(administratorRQ.getLastName())
                .password(administratorRQ.getPassword())
                .passwordConfirmation(administratorRQ.getPasswordConfirmation())
                .email(administratorRQ.getEmail())
                .groups(List.of(Groups.ADMIN_GROUP))
                .enabled(true)
                .build();
        try {
            oAuth2Service.createUser(build);
            return oAuth2Service.getUUIDByUsername(administratorRQ.getUsername());
        } catch (UnavailableException e) {
            log.error("e", e);
            throw e;
        }
    }
}
