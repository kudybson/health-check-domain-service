package pl.akh.services;

import pl.akh.model.common.Specialization;
import pl.akh.model.rq.DoctorRQ;
import pl.akh.model.rs.DoctorRS;
import pl.akh.model.rs.RatingRS;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface DoctorService {
    DoctorRS createDoctor(DoctorRQ doctorRQ) throws Exception;

    Optional<DoctorRS> getDoctorById(UUID doctorUUID);

    Collection<DoctorRS> getDoctorsByCriteria(Specialization specialization, Long departmentId,
                                              String firstname, String lastName);

    Collection<RatingRS> getDoctorRates(UUID doctorUUID) throws Exception;

    void deleteDoctor(UUID doctorUUID);
}
