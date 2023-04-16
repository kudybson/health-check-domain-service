package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "REFERRAL")
@NoArgsConstructor
@Getter
@Setter
public class Referral implements Serializable {

    private static final long serialVersionUID = 10L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "referral_seq_generator")
    @SequenceGenerator(name = "referral_seq_generator", sequenceName = "referral_seq", allocationSize = 1)
    @Column(name = "REFERRAL_ID")
    private Long id;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE}, targetEntity = Treatment.class)
    @JoinColumn(name = "TREATMENT_ID", referencedColumnName = "TREATMENT_ID")
    @LazyGroup("treatment")
    @NotNull
    private Treatment treatment;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TestType testType;

    @Column(name = "EXPIRATION_DATE", nullable = false)
    @NotNull
    private Timestamp expirationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Referral referral = (Referral) o;
        return id.equals(referral.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
