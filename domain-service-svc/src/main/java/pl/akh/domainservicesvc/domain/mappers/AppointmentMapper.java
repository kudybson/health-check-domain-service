package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Appointment;
import pl.akh.domainservicesvc.domain.repository.AppointmentRepository;
import pl.akh.model.rs.AppointmentRS;
import pl.akh.model.rs.schedules.AppointmentDateRS;

public class AppointmentMapper {

    public static AppointmentDateRS mapToAppointmentDateDto(Appointment appointment) {
        return AppointmentDateRS.builder()
                .startDateTime(appointment.getAppointmentDate().toLocalDateTime())
                .endDateTime(appointment.getAppointmentDate().toLocalDateTime().plus(AppointmentRepository.APPOINTMENT_TIME))
                .build();
    }

    public static AppointmentRS mapToDto(Appointment appointment) {
        if (appointment == null) return null;
        return AppointmentRS.builder()
                .id(appointment.getId())
                .doctorRS(DoctorMapper.mapToDto(appointment.getDoctor()))
                .patientRS(PatientMapper.mapToDto(appointment.getPatient()))
                .appointmentDate(appointment.getAppointmentDate())
                .comments(appointment.getComments())
                .status(appointment.getStatus().toDto())
                .departmentId(appointment.getDepartment().getId())
                .treatmentRS(TreatmentMapper.mapToDto(appointment.getTreatment()))
                .build();
    }
}
