package pl.akh.domainservicesvc.domain.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.exceptions.DepartmentNotFoundException;
import pl.akh.domainservicesvc.domain.exceptions.PasswordConfirmationException;
import pl.akh.domainservicesvc.domain.mappers.DoctorMapper;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.repository.DoctorRepository;
import pl.akh.model.common.Specialization;
import pl.akh.model.rq.DoctorRQ;
import pl.akh.model.rq.ScheduleRQ;
import pl.akh.model.rs.DoctorRS;
import pl.akh.model.rs.RatingRS;
import pl.akh.model.rs.schedules.ScheduleRS;
import pl.akh.model.rs.schedules.SchedulesAppointmentsRS;
import pl.akh.services.DoctorService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final StuffServiceImpl stuffService;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(StuffServiceImpl stuffService, DepartmentRepository departmentRepository, DoctorRepository doctorRepository) {
        this.stuffService = stuffService;
        this.departmentRepository = departmentRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorRS createDoctor(DoctorRQ doctorRQ) throws Exception {
        if (!Objects.equals(doctorRQ.getPassword(), doctorRQ.getPasswordConfirmation())) {
            throw new PasswordConfirmationException("Passwords are not the same.");
        }
        Department department = departmentRepository.findById(doctorRQ.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(String.format("Department with id: %d not found.", doctorRQ.getDepartmentId())));

        UUID doctorUUID = stuffService.addStuffMember(doctorRQ);

        Doctor doctor = new Doctor();
        doctor.setId(doctorUUID);
        return DoctorMapper.mapToDto(doctor);
    }

    @Override
    public Collection<DoctorRS> getAllDoctors() {
        return null;
    }

    @Override
    public Collection<DoctorRS> getAllDoctors(long pageNumber, long pageSize) {
        return null;
    }

    @Override
    public DoctorRS getDoctorById(UUID doctorUUID) {
        return null;
    }

    @Override
    public Collection<DoctorRS> getDoctorsByDepartment(long departmentId) {
        return null;
    }

    @Override
    public Collection<DoctorRS> getDoctorsByName(String firstName, String lastName) {
        return null;
    }

    @Override
    public Collection<DoctorRS> getDoctorsBySpecialization(Specialization specialization) {
        return null;
    }

    @Override
    public Collection<ScheduleRS> getSchedulesByDoctorIdBetweenDates(UUID doctorUUID, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public SchedulesAppointmentsRS getSchedulesWithAppointmentByDoctorId(UUID doctorUUID, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public Collection<ScheduleRS> insertSchedules(UUID doctorUUID, Collection<ScheduleRQ> schedules) {
        return null;
    }

    @Override
    public Collection<RatingRS> getDoctorRates(UUID doctorUUID) {
        return null;
    }

    @Override
    public void deleteDoctor(UUID doctorUUID) throws Exception {

    }
}
