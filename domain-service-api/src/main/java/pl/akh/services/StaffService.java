package pl.akh.services;

import pl.akh.model.rq.AdministratorRQ;
import pl.akh.model.rq.DoctorRQ;
import pl.akh.model.rq.ReceptionistRQ;
import pl.akh.model.rs.AdministratorRS;
import pl.akh.model.rs.DoctorRS;
import pl.akh.model.rs.ReceptionistRS;

import java.util.UUID;

public interface StaffService {
    AdministratorRS addAdministrator(AdministratorRQ administratorRQ) throws Exception;

    ReceptionistRS createReceptionist(ReceptionistRQ receptionistRQ) throws Exception;

    DoctorRS createDoctor(DoctorRQ doctorRQ) throws Exception;

    void deleteStaffMember(UUID staffMemberUUID) throws Exception;

    void lockStaffMemberAccount(UUID receptionistUUID) throws Exception;

    void unlockStaffMemberAccount(UUID receptionistUUID) throws Exception;
}
