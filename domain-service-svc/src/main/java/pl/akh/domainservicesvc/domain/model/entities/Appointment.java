package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "APPOINTMENT")
@NoArgsConstructor
@Getter
@Setter
public class Appointment implements Serializable {

    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_seq_generator")
    @SequenceGenerator(name = "appointment_seq_generator", sequenceName = "appointment_seq", allocationSize = 1)
    @Column(name = "APPOINTMENT_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = Doctor.class)
    @JoinColumn(name = "DOCTOR_ID", referencedColumnName = "DOCTOR_ID")
    @LazyGroup("doctor")
    @NotNull
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = Patient.class)
    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "PATIENT_ID")
    @LazyGroup("patient")
    @NotNull
    private Patient patient;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, targetEntity = Department.class)
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID")
    @LazyGroup("department")
    @NotNull
    private Department department;

    @Column(name = "APPOINTMENT_DATE")
    @NotNull
    private Timestamp appointmentDate;

    @Column(name = "COMMENTS")
    private String comments;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "APPOINTMENT_ID", referencedColumnName = "APPOINTMENT_ID")
    @LazyGroup("treatment")
    private Treatment treatment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
