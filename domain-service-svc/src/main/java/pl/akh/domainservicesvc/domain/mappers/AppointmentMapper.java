package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Appointment;
import pl.akh.domainservicesvc.domain.repository.AppointmentRepository;
import pl.akh.model.rs.schedules.AppointmentDateRS;

public class AppointmentMapper {

    public static AppointmentDateRS mapToDto(Appointment appointment) {
        return AppointmentDateRS.builder()
                .startDateTime(appointment.getAppointmentDate().toLocalDateTime())
                .endDateTime(appointment.getAppointmentDate().toLocalDateTime().plus(AppointmentRepository.APPOINTMENT_TIME))
                .build();
    }
}
