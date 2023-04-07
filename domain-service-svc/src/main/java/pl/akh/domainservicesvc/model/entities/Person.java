package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class Person {

    @Id
    //todo dodawanie osoby do bazy nie działa przez uuid
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "PERSON_ID", columnDefinition = "BINARY(16)")
    @org.hibernate.validator.constraints.UUID
    private UUID id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "SECOND_NAME")
    private String secondName;
}
