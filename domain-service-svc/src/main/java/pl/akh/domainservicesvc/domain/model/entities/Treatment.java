package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TREATMENT")
@NoArgsConstructor
@Getter
@Setter
public class Treatment implements Serializable {

    private static final long serialVersionUID = 14L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treatment_seq_generator")
    @SequenceGenerator(name = "treatment_seq_generator", sequenceName = "treatment_seq", allocationSize = 1)
    @Column(name = "TREATMENT_ID")
    private Long id;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE}, targetEntity = Appointment.class)
    @JoinColumn(name = "APPOINTMENT_ID", referencedColumnName = "APPOINTMENT_ID")
    @LazyGroup("appointment")
    @NotNull
    private Appointment appointment;

    @Column(name = "DIAGNOSIS", nullable = false)
    @NotEmpty
    private String diagnosis;

    @Column(name = "RECOMMENDATION", nullable = false)
    @NotEmpty
    private String recommendation;

    @OneToOne(mappedBy = "treatment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TREATMENT_ID", referencedColumnName = "TREATMENT_ID")
    @LazyGroup("prescription")
    private Prescription prescription;

    @OneToOne(mappedBy = "treatment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TREATMENT_ID", referencedColumnName = "TREATMENT_ID")
    @LazyGroup("referral")
    private Referral referral;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Treatment treatment = (Treatment) o;
        return id.equals(treatment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
