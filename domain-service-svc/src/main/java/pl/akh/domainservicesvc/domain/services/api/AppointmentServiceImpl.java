package pl.akh.domainservicesvc.domain.services.api;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akh.domainservicesvc.domain.exceptions.AppointmentConflictException;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PatientNotFoundException;
import pl.akh.domainservicesvc.domain.mappers.AppointmentMapper;
import pl.akh.domainservicesvc.domain.model.entities.Appointment;
import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.domainservicesvc.domain.model.entities.Patient;
import pl.akh.domainservicesvc.domain.model.entities.Status;
import pl.akh.domainservicesvc.domain.repository.AppointmentRepository;
import pl.akh.domainservicesvc.domain.repository.DoctorRepository;
import pl.akh.domainservicesvc.domain.repository.PatientRepository;
import pl.akh.domainservicesvc.domain.repository.ScheduleRepository;
import pl.akh.model.rq.AppointmentRQ;
import pl.akh.model.rs.AppointmentRS;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;
import pl.akh.services.AppointmentService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.akh.domainservicesvc.domain.repository.AppointmentRepository.APPOINTMENT_TIME;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ScheduleRepository scheduleRepository;
    private final NotificationService notificationService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, ScheduleRepository scheduleRepository, NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.scheduleRepository = scheduleRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppointmentRS> getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(AppointmentMapper::mapToDto);
    }

    @Override
    public AppointmentRS createAppointment(AppointmentRQ appointmentRQ) throws Exception {
        if (appointmentRQ.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException();
        }
        Doctor doctor = doctorRepository.findById(appointmentRQ.getDoctorUUID())
                .orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id: %s not found.", appointmentRQ.getDoctorUUID())));
        Patient patient = patientRepository.findById(appointmentRQ.getPatientUUID())
                .orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id: %s not found.", appointmentRQ.getPatientUUID())));

        LocalDateTime appointmentDateTime = appointmentRQ.getAppointmentDateTime().truncatedTo(ChronoUnit.MINUTES);
        final Timestamp appointmentDateTimestamp = Timestamp.valueOf(appointmentDateTime);
        final Timestamp appointmentEndDateTimestamp = Timestamp.valueOf(appointmentDateTime.plusMinutes(APPOINTMENT_TIME.toMinutes()));

        if ((appointmentRQ.getAppointmentDateTime().getMinute() % APPOINTMENT_TIME.toMinutes()) != 0) {
            throw new IllegalArgumentException();
        }
        if (appointmentRepository.findAppointmentByDoctorIdAndAppointmentDate(doctor.getId(), appointmentDateTimestamp, Status.SCHEDULED).isPresent()) {
            throw new AppointmentConflictException();
        }
        if (scheduleRepository.getScheduleByDoctorIdAndStartDateTimeAndEndDateTime(doctor.getId(), appointmentDateTimestamp,
                appointmentEndDateTimestamp).isEmpty()) {
            throw new AppointmentConflictException();
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(appointmentDateTimestamp);
        appointment.setStatus(Status.SCHEDULED);
        appointment.setDepartment(doctor.getDepartment());
        Appointment save = appointmentRepository.save(appointment);
        return AppointmentMapper.mapToDto(save);
    }

    @Override
    public void removeAppointmentById(Long id) throws Exception {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        if (appointment.getAppointmentDate().before(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new IllegalArgumentException();
        }
        appointment.setStatus(Status.CANCELED);
        Notification notification = new Notification();
        notification.setUserId(appointment.getPatient().getId());
        notification.setPayload("Your appointment in " + appointment.getDepartment().getName() + " of " + appointment.getAppointmentDate() +
                " with doctor " + appointment.getDoctor().getLastName() + " has been cancelled");
        notificationService.sendNotification(notification);
    }

    @Override
    public Collection<AppointmentRS> getAppointmentsByDoctorId(UUID doctorUUID, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.getAppointmentsByDoctorId(doctorUUID, Timestamp.valueOf(start), Timestamp.valueOf(end), Status.SCHEDULED)
                .stream()
                .map(AppointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<AppointmentRS> getAppointmentsByPatientId(UUID patientUUID, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.getAppointmentsByPatientId(patientUUID, Timestamp.valueOf(start), Timestamp.valueOf(end), Status.SCHEDULED)
                .stream()
                .map(AppointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<AppointmentRS> getAppointmentsByDepartmentId(Long id, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.getAppointmentsByDepartmentId(id, Timestamp.valueOf(start), Timestamp.valueOf(end), Status.SCHEDULED)
                .stream()
                .map(AppointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentRS addCommentToAppointment(Long id, String comment) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        appointment.setComments(comment);
        return AppointmentMapper.mapToDto(appointment);
    }
}
