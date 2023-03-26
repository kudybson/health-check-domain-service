package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

@Entity(name = "REFERRAL")
@NoArgsConstructor
@Getter
@Setter
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFERRAL_ID")
    private Long id;

    @OneToOne(mappedBy = "referral", cascade = CascadeType.DETACH)
    @LazyGroup("treatment")
    private Treatment treatment;
}
