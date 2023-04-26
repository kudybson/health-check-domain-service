package pl.akh.domainservicesvc.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PatientNotFoundException;
import pl.akh.domainservicesvc.domain.mappers.AppointmentMapper;
import pl.akh.domainservicesvc.domain.model.entities.Appointment;
import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.domainservicesvc.domain.model.entities.Patient;
import pl.akh.domainservicesvc.domain.repository.AppointmentRepository;
import pl.akh.domainservicesvc.domain.repository.DoctorRepository;
import pl.akh.domainservicesvc.domain.repository.PatientRepository;
import pl.akh.model.rq.AppointmentRQ;
import pl.akh.model.rs.AppointmentRS;
import pl.akh.services.AppointmentService;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppointmentRS> getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(AppointmentMapper::mapToDto);
    }

    @Override
    public AppointmentRS createAppointment(AppointmentRQ appointmentRQ) throws Exception {
        Doctor doctor = doctorRepository.findById(appointmentRQ.getDoctorUUID())
                .orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id: %s not found.", appointmentRQ.getDoctorUUID())));
        Patient patient = patientRepository.findById(appointmentRQ.getPatientUUID())
                .orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id: %s not found.", appointmentRQ.getPatientUUID())));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(Timestamp.valueOf(appointmentRQ.getAppointmentDateTime()));
        Appointment save = appointmentRepository.save(appointment);
        return AppointmentMapper.mapToDto(save);
    }

    @Override
    public AppointmentRS removeAppointmentById(Long id) {
        return null;
    }

    @Override
    public Collection<AppointmentRS> getAppointmentsByDoctorId(UUID doctorUUID) {
        return null;
    }

    @Override
    public Collection<AppointmentRS> getAppointmentsByPatientId(UUID patientUUID) {
        return null;
    }

    @Override
    public Collection<AppointmentRS> getAppointmentsByDepartmentId(Long id) {
        return null;
    }

    @Override
    public AppointmentRS addCommentToAppointment(Long appointmentId, String comment) {
        return null;
    }
}
