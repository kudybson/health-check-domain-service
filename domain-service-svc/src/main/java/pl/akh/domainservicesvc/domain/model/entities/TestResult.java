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
    @NotEmpty
    private String description;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE}, targetEntity = MedicalTest.class)
    @JoinColumn(name = "TEST_ID", referencedColumnName = "TEST_ID")
    @LazyGroup("test")
    @NotNull
    private MedicalTest medicalTest;

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
