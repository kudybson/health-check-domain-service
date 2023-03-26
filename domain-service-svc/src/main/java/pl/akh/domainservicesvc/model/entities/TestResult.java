package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

@Entity(name = "TEST_RESULT")
@NoArgsConstructor
@Getter
@Setter
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEST_RESULT_ID")
    private Long id;

    @OneToOne(mappedBy = "testResult", cascade = CascadeType.DETACH)
    @LazyGroup("test")
    private Test test;
}
