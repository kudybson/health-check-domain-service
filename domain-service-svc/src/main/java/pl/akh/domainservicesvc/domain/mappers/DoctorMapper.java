package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.model.rs.DoctorRS;

public class DoctorMapper {

    public static DoctorRS mapToDto(Doctor entity) {
        if (entity == null) return null;
        return DoctorRS.builder()
                .doctorUUID(entity.getId())
                .firstname(entity.getFirstName())
                .lastname(entity.getLastName())
                .departmentId(entity.getDepartment().getId())
                .specialization(entity.getSpecialization().toDto())
                .build();
    }
}
