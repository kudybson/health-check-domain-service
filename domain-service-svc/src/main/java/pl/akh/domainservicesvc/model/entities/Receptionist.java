package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

@Entity(name = "RECEPTIONIST")
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "RECEPTIONIST_ID")
public class Receptionist extends Person {

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("department")
    private Department department;
}
