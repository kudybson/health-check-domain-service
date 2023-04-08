package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "PATIENT")
@NoArgsConstructor
@Getter
@Setter
@SelectBeforeUpdate(value=false)
@PrimaryKeyJoinColumn(name = "PATIENT_ID")
public class Patient extends Person implements Serializable {

    private static final long serialVersionUID = 6L;

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
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @LazyGroup("rating")
    private Set<Rating> ratings;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @LazyGroup("test")
    private Set<Test> tests;
}
