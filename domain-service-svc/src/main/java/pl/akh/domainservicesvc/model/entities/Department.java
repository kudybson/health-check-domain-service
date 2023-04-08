package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "DEPARTMENT")
@NoArgsConstructor
@Getter
@Setter
@SelectBeforeUpdate(value = false)
public class Department implements Serializable {

    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq_generator")
    @SequenceGenerator(name = "department_seq_generator", sequenceName = "department_seq", allocationSize = 1)
    @Column(name = "DEPARTMENT_ID")
    private Long id;

    @Column(name = "NAME")
    @NotEmpty
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Address.class)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    @LazyGroup("address")
    @NotNull
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID")
    @LazyGroup("doctor")
    private Set<Doctor> doctors;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID")
    @LazyGroup("receptionist")
    private Collection<Receptionist> receptionists;

    @OneToOne(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @LazyGroup("administrator")
    private Administrator administrator;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department")
    @LazyGroup("appointment")
    private Collection<Appointment> appointments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID")
    @LazyGroup("test")
    private Collection<Test> tests;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
