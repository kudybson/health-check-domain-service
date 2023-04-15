package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "PRESCRIPTION")
@NoArgsConstructor
@Getter
@Setter
public class Prescription implements Serializable {

    private static final long serialVersionUID = 7L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prescription_seq_generator")
    @SequenceGenerator(name = "prescription_seq_generator", sequenceName = "prescription_seq", allocationSize = 1)
    @Column(name = "PRESCRIPTION_ID")
    private Long id;

    @OneToOne(mappedBy = "prescription", cascade = CascadeType.DETACH)
    @LazyGroup("treatment")
    private Treatment treatment;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EXPIRATION_DATE")
    private LocalDate expirationDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
