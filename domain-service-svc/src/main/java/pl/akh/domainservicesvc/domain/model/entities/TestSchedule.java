package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "TEST_SCHEDULE")
@NoArgsConstructor
@Getter
@Setter
public class TestSchedule implements Serializable {

    private static final long serialVersionUID = 15L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_schedule_seq_generator")
    @SequenceGenerator(name = "test_schedule_seq_generator", sequenceName = "test_schedule_seq", allocationSize = 1)
    @Column(name = "TEST_SCHEDULE_ID")
    private Long id;

    @Column(name = "TYPE")
    @Enumerated(value = EnumType.STRING)
    private TestType type;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "DEPARTMENT_ID")
    @LazyGroup("department")
    private Department department;

    @Column(name = "START_DATE_TIME")
    private Timestamp startDateTime;

    @Column(name = "END_DATE_TIME")
    private Timestamp endDateTime;
}
