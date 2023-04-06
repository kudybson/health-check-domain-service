package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "SCHEDULE")
@NoArgsConstructor
@Getter
@Setter
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_seq_generator")
    @SequenceGenerator(name = "schedule_seq_generator", sequenceName = "schedule_seq", allocationSize = 1)
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("doctor")
    private Doctor doctor;

    @Column(name = "START_DATE_TIME")
    private LocalDateTime startDateTime;

    @Column(name = "END_DATE_TIME")
    private LocalDateTime endDateTime;
}
