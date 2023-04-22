package pl.akh.domainservicesvc.domain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.domainservicesvc.domain.model.entities.Specialization;
import pl.akh.domainservicesvc.domain.repository.DoctorRepository;
import pl.akh.domainservicesvc.domain.repository.ScheduleRepository;
import pl.akh.model.rq.ScheduleRQ;
import pl.akh.model.rs.schedules.ScheduleRS;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    DoctorRepository doctorRepository;

    @InjectMocks
    ScheduleServiceImpl scheduleService;

    @Test
    void shouldCreateSchedules() {
        //given
        UUID doctorUUID = UUID.randomUUID();
        Doctor doctor = createDoctor(doctorUUID);
        when(doctorRepository.findById(doctorUUID)).thenReturn(Optional.of(doctor));
        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(1, 15), fromHHMM(1, 30)),
                create(fromHHMM(2, 15), fromHHMM(2, 30)));
        //when

        Collection<ScheduleRS> scheduleRS = scheduleService.insertSchedules(doctorUUID, schedules);

        //then
        verify(scheduleRepository, times(1)).saveAll(any());
    }

    @Test
    void shouldNotCreateSchedulesWithSameTimes() {
        //given
        UUID doctorUUID = UUID.randomUUID();
        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(1, 15), fromHHMM(1, 30)),
                create(fromHHMM(1, 15), fromHHMM(1, 30)));
        //when

        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.insertSchedules(doctorUUID, schedules);
        });

        //then
        verify(scheduleRepository, times(0)).saveAll(any());
    }

    @Test
    void shouldNotCreateScheduleWhenStartDateIsAfterEndDate() {
        //given
        UUID doctorUUID = UUID.randomUUID();

        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(1, 45), fromHHMM(1, 30)));

        //when
        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.insertSchedules(doctorUUID, schedules);
        });

        //then
        verify(scheduleRepository, times(0)).saveAll(any());
    }

    @Test
    void shouldNotCreateSchedulesCauseChronologyOverlapping() {
        //given
        UUID doctorUUID = UUID.randomUUID();
        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(1, 0), fromHHMM(1, 30)),
                create(fromHHMM(1, 15), fromHHMM(1, 45)));

        //when
        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.insertSchedules(doctorUUID, schedules);
        });

        //then
        verify(scheduleRepository, times(0)).saveAll(any());
    }

    @Test
    void shouldNotCreateScheduleWhenDateTimeIsNotValidScheduleTime() {
        //given
        UUID doctorUUID = UUID.randomUUID();
        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(1, 1), fromHHMM(1, 30)),
                create(fromHHMM(2, 15), fromHHMM(2, 45)));

        //when
        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.insertSchedules(doctorUUID, schedules);
        });

        //then
        verify(scheduleRepository, times(0)).saveAll(any());
    }

    @Test
    void shouldNotCreateScheduleWhenAnyIsOutdated() {
        //given
        UUID doctorUUID = UUID.randomUUID();
        List<ScheduleRQ> schedules = List.of(create(fromHHMM(1, 0).minusDays(5), fromHHMM(1, 30).minusDays(5)));
        //when
        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.insertSchedules(doctorUUID, schedules);
        });
        //then
        verify(scheduleRepository, times(0)).saveAll(any());
    }

    @Test
    public void shouldThrowDoctorNotFoundExceptionWhenSearchingForSchedulesWithAppointment() {
        //given
        UUID doctorUUID = UUID.randomUUID();
        when(doctorRepository.existsById(doctorUUID)).thenReturn(false);
        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(1, 15), fromHHMM(1, 30)),
                create(fromHHMM(2, 15), fromHHMM(2, 30)));
        //when

        assertThrows(DoctorNotFoundException.class, () -> {
            scheduleService.getSchedulesWithAppointmentByDoctorId(doctorUUID, LocalDateTime.now(), LocalDateTime.now());
        });
        verify(scheduleRepository, times(0)).saveAll(any());
    }

    @Test
    public void shouldThrowDoctorNotFoundExceptionWhenSearchingForSchedules() {
        //given
        UUID doctorUUID = UUID.randomUUID();
        when(doctorRepository.existsById(doctorUUID)).thenReturn(false);
        List<ScheduleRQ> schedules = List.of(
                create(fromHHMM(1, 15), fromHHMM(1, 30)),
                create(fromHHMM(2, 15), fromHHMM(2, 30)));
        //when

        assertThrows(DoctorNotFoundException.class, () -> {
            scheduleService.getSchedulesByDoctorIdBetweenDates(doctorUUID, LocalDateTime.now(), LocalDateTime.now());
        });
        verify(scheduleRepository, times(0)).saveAll(any());
    }

    private LocalDateTime fromHHMM(int h, int m) {
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), h, m);
    }

    private ScheduleRQ create(LocalDateTime start, LocalDateTime end) {
        return ScheduleRQ.builder()
                .startDateTime(start)
                .endDateTime(end)
                .build();
    }

    private Doctor createDoctor(UUID doctorUUID) {
        Doctor doctor = new Doctor();
        doctor.setId(doctorUUID);
        doctor.setSpecialization(Specialization.ANESTHESIA);
        doctor.setFirstName("Johny");
        doctor.setLastName("Wick");
        return doctor;
    }
}