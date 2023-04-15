package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.util.Objects;

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
    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    @LazyGroup("doctor")
    @NotNull
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PATIENT_ID", nullable = false)
    @LazyGroup("patient")
    @NotNull
    private Patient patient;

    @Column(name = "GRADE", nullable = false)
    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    private Long grade;

    @Column(name = "DESCRIPTION")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return id.equals(rating.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
