package pl.akh.services;

import pl.akh.model.rq.AdministratorRQ;
import pl.akh.model.rs.AdministratorRS;

import java.util.Optional;
import java.util.UUID;

public interface AdministratorService {
    AdministratorRS addAdministrator(AdministratorRQ administratorRQ) throws Exception;

    Optional<AdministratorRS> getAdministratorById(UUID administratorUUID) throws Exception;
    Optional<AdministratorRS> getAdministratorByDepartmentId(Long departmentId) throws Exception;

    void deleteAdministrator(UUID administratorUUID) throws Exception;
}
