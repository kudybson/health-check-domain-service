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
@Table(name = "SCHEDULE")
@NoArgsConstructor
@Getter
@Setter
public class Schedule implements Serializable {

    private static final long serialVersionUID = 11L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_seq_generator")
    @SequenceGenerator(name = "schedule_seq_generator", sequenceName = "schedule_seq", allocationSize = 1)
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    @LazyGroup("doctor")
    @NotNull
    private Doctor doctor;

    @Column(name = "START_DATE_TIME", nullable = false)
    @NotNull
    private Timestamp startDateTime;

    @Column(name = "END_DATE_TIME", nullable = false)
    @NotNull
    private Timestamp endDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return id.equals(schedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
