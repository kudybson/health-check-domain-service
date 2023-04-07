package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;

@Entity
@Table(name = "RATING")
@NoArgsConstructor
@Getter
@Setter
public class Rating implements Serializable {

    private static final long serialVersionUID = 8L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_seq_generator")
    @SequenceGenerator(name = "rating_seq_generator", sequenceName = "rating_seq", allocationSize = 1)
    @Column(name = "RATING_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCTOR_ID")
    @LazyGroup("doctor")
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PATIENT_ID")
    @LazyGroup("patient")
    private Patient patient;

    @Column(name = "GRADE")
    private Long grade;

    @Column(name = "DESCRIPTION")
    private String description;
}
