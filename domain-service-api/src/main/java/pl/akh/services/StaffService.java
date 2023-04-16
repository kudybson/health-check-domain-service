package pl.akh.services;

import pl.akh.model.rq.AdministratorRQ;
import pl.akh.model.rq.DoctorRQ;
import pl.akh.model.rq.ReceptionistRQ;
import pl.akh.model.rs.AdministratorRS;
import pl.akh.model.rs.DoctorRS;
import pl.akh.model.rs.ReceptionistRS;

import java.util.UUID;

public interface StaffService {
    AdministratorRS addAdministrator(AdministratorRQ administratorRQ);

    ReceptionistRS createReceptionist(ReceptionistRQ receptionistRQ);

    DoctorRS createDoctor(DoctorRQ doctorRQ);

    void deleteStaffMember(UUID staffMemberUUID);

    void lockStaffMemberAccount(UUID receptionistUUID);

    void unlockStaffMemberAccount(UUID receptionistUUID);
}
