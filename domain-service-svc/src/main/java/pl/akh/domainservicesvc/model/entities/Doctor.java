package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@SelectBeforeUpdate(value = false)
@PrimaryKeyJoinColumn(name = "DOCTOR_ID")
public class Doctor extends Person implements Serializable {

    private static final long serialVersionUID = 5L;

    @ManyToOne(cascade = {CascadeType.DETACH}, targetEntity = Department.class)
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID", nullable = false)
    @NotNull
    private Department department;

    @Column(name = "SPECIALIZATION", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Specialization specialization;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    @LazyGroup("schedule")
    private Set<Schedule> schedules = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor", fetch = FetchType.LAZY)
    @LazyGroup("appointment")
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctor")
    @LazyGroup("rating")
    private Set<Rating> ratings = new HashSet<>();
}
