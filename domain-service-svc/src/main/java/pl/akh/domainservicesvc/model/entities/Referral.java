package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;

@Entity
@Table(name = "REFERRAL")
@NoArgsConstructor
@Getter
@Setter
public class Referral implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "referral_seq_generator")
    @SequenceGenerator(name = "referral_seq_generator", sequenceName = "referral_seq", allocationSize = 1)
    @Column(name = "REFERRAL_ID")
    private Long id;

    @OneToOne(mappedBy = "referral", cascade = CascadeType.DETACH)
    @LazyGroup("treatment")
    private Treatment treatment;
}
