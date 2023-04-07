package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.io.Serializable;
import java.util.Collection;

@Entity(name = "DOCTOR")
@NoArgsConstructor
@Getter
@Setter
@SelectBeforeUpdate(value=false)
@PrimaryKeyJoinColumn(name = "DOCTOR_ID")
public class Doctor extends Person implements Serializable {

    private static final long serialVersionUID = 5L;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    @LazyGroup("department")
    private Department department;

    @Column(name = "SPECIALIZATION")
    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    @LazyGroup("schedule")
    private Collection<Schedule> schedules;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor", fetch = FetchType.LAZY)
    @LazyGroup("appointment")
    private Collection<Appointment> appointments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    @LazyGroup("rating")
    private Collection<Rating> ratings;
}
