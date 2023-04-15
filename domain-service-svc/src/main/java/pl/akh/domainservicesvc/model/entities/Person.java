package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "PERSON")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@SelectBeforeUpdate(value = false)
public abstract class Person {

    @Id
    @Column(name = "PERSON_ID", columnDefinition = "RAW(16)", nullable = false, unique = true)
    private UUID id;

    @Column(name = "FIRST_NAME", nullable = false)
    @NotEmpty
    private String firstName;

    @Column(name = "SECOND_NAME", nullable = false)
    @NotEmpty
    private String secondName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
