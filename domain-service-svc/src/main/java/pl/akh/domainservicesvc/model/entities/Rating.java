package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

@Entity(name = "RATING")
@NoArgsConstructor
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RATING_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("doctor")
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("patient")
    private Patient patient;

    @Column(name = "GRADE")
    private Long grade;

    @Column(name = "DESCRIPTION")
    private String description;
}
