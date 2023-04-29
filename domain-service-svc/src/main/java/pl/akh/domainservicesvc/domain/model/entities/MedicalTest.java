package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.annotation.Nullable;
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
@Table(name = "TEST")
@NoArgsConstructor
@Getter
@Setter
public class MedicalTest implements Serializable {

    private static final long serialVersionUID = 12L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_seq_generator")
    @SequenceGenerator(name = "test_seq_generator", sequenceName = "test_seq", allocationSize = 1)
    @Column(name = "TEST_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "DEPARTMENT_ID")
    @LazyGroup("department")
    @NotNull
    private Department department;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "PATIENT_ID")
    @LazyGroup("patient")
    @NotNull
    private Patient patient;

    @Column(name = "TYPE")
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private TestType type;

    @Column(name = "TEST_DATE")
    @NotNull
    private Timestamp testDate;

    @OneToOne(mappedBy = "medicalTest", cascade = CascadeType.ALL)
    @JoinColumn(name = "TEST_ID", referencedColumnName = "TEST_ID")
    @LazyGroup("testResult")
    @Nullable
    private MedicalTestResult medicalTestResult;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalTest medicalTest = (MedicalTest) o;
        return id.equals(medicalTest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
