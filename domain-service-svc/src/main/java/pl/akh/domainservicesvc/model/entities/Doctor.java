package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "DOCTOR")
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "DOCTOR_ID")
public class Doctor extends Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("department")
    private Department department;

    @Column(name = "SPECIALIZATION")
    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    @LazyGroup("schedule")
    private Collection<Schedule> schedules;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    @LazyGroup("appointment")
    private Collection<Appointment> appointments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    @LazyGroup("rating")
    private Collection<Rating> ratings;
}
