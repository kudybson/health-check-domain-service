package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.time.LocalDate;

@Entity(name = "PRESCRIPTION")
@NoArgsConstructor
@Getter
@Setter
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
