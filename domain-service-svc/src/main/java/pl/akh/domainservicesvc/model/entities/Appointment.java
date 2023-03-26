package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.time.LocalDateTime;

@Entity(name = "APPOINTMENT")
@NoArgsConstructor
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPOINTMENT_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @LazyGroup("doctor")
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @LazyGroup("patient")
    private Patient patient;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @LazyGroup("department")
    private Department department;

    @Column(name = "APPOINTMENT_DATE")
    private LocalDateTime appointmentDate;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "CABINET_NUMBER")
    private Long cabinetNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "APPOINTMENT_ID", referencedColumnName = "APPOINTMENT_ID")
    @LazyGroup("treatment")
    private Treatment treatment;
}
