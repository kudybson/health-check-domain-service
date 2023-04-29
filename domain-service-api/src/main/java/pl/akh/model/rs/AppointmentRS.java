package pl.akh.model.rs;

import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.Status;

import java.sql.Timestamp;

@Data
@Builder
public class AppointmentRS {
    private Long id;
    private DoctorRS doctorRS;
    private PatientRS patientRS;
    private Long departmentId;
    private Status status;
    private Timestamp appointmentDate;
    private String comments;
    private TreatmentRS treatmentRS;
}
