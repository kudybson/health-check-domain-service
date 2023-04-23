package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "PATIENT")
@NoArgsConstructor
@Getter
@Setter
@SelectBeforeUpdate(value = false)
@PrimaryKeyJoinColumn(name = "PATIENT_ID")
public class Patient extends Person implements Serializable {

    private static final long serialVersionUID = 6L;

    //@PESEL should we replace pesel by sth more international?
    @Column(name = "PESEL")
    @NotEmpty
    private String pesel;

    @Column(name = "PHONE_NUMBER")
    @NotEmpty
    private String phoneNumber;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    @LazyGroup("address")
    @NotNull
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    @LazyGroup("appointment")
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @LazyGroup("rating")
    private Set<Rating> ratings;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @LazyGroup("test")
    private Set<MedicalTest> medicalTests;

    public Patient(UUID id) {
        super(id);
    }
}
