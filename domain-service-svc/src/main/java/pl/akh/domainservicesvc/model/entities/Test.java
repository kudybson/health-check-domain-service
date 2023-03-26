package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

@Entity(name = "TEST")
@NoArgsConstructor
@Getter
@Setter
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEST_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("department")
    private Department department;

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("patient")
    private Patient patient;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_ID", referencedColumnName = "TEST_ID")
    @LazyGroup("testResult")
    private TestResult testResult;
}
