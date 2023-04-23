package pl.akh.services;

import pl.akh.model.rq.ReceptionistRQ;
import pl.akh.model.rs.ReceptionistRS;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ReceptionistService {
    ReceptionistRS createReceptionist(ReceptionistRQ receptionistRQ) throws Exception;

    Optional<ReceptionistRS> getReceptionistByUUID(UUID receptionistUUID) throws Exception;

    void deleteReceptionist(UUID receptionistUUID) throws Exception;

    Collection<ReceptionistRS> getReceptionistsByDepartmentId(Long departmentId);

}
