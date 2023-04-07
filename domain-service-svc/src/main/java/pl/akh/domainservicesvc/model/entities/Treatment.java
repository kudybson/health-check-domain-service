package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;

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

    @OneToOne(mappedBy = "treatment", cascade = CascadeType.DETACH)
    @LazyGroup("appointment")
    private Appointment appointment;

    @Column(name = "DIAGNOSIS")
    private String diagnosis;

    @Column(name = "RECOMMENDATION")
    private String recommendation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TREATMENT_ID", referencedColumnName = "TREATMENT_ID")
    @LazyGroup("prescription")
    private Prescription prescription;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TREATMENT_ID", referencedColumnName = "TREATMENT_ID")
    @LazyGroup("referral")
    private Referral referral;
}
