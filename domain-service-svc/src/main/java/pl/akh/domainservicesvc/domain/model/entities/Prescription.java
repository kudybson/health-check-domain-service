package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.sql.Timestamp;
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

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE}, targetEntity = Treatment.class)
    @JoinColumn(name = "TREATMENT_ID", referencedColumnName = "TREATMENT_ID")
    @LazyGroup("treatment")
    @NotNull
    private Treatment treatment;

    @Column(name = "CODE")
    @NotEmpty
    private String code;

    @Column(name = "DESCRIPTION")
    @NotEmpty
    private String description;

    @Column(name = "EXPIRATION_DATE")
    @NotNull
    private Timestamp expirationDate;

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
