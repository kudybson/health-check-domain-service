package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "PATIENT")
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "PATIENT_ID")
public class Patient extends Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "PESEL")
    private String pesel;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    @LazyGroup("address")
    private Address address;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    @LazyGroup("appointment")
    private Collection<Appointment> appointments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @LazyGroup("rating")
    private Collection<Rating> ratings;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @LazyGroup("test")
    private Collection<Test> tests;
}
