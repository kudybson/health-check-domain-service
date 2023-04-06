package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;

@Entity
@Table(name = "TEST")
@NoArgsConstructor
@Getter
@Setter
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_seq_generator")
    @SequenceGenerator(name = "test_seq_generator", sequenceName = "test_seq", allocationSize = 1)
    @Column(name = "TEST_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("department")
    private Department department;

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("patient")
    private Patient patient;

    @Column(name = "TYPE")
    @Enumerated(value = EnumType.STRING)
    private TestType type;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_ID", referencedColumnName = "TEST_ID")
    @LazyGroup("testResult")
    private TestResult testResult;
}
