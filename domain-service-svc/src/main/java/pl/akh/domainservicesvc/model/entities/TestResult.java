package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TEST_RESULT")
@NoArgsConstructor
@Getter
@Setter
public class TestResult implements Serializable {

    private static final long serialVersionUID = 13L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_result_seq_generator")
    @SequenceGenerator(name = "test_result_seq_generator", sequenceName = "test_result_seq", allocationSize = 1)
    @Column(name = "TEST_RESULT_ID")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(mappedBy = "testResult", cascade = CascadeType.DETACH)
    @LazyGroup("test")
    private Test test;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult that = (TestResult) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
