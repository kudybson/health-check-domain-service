package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

@Entity(name = "ADMINISTRATOR")
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "ADMINISTRATOR_ID")
public class Administrator extends Person {

    @OneToOne(mappedBy = "administrator", cascade = CascadeType.DETACH)
    @LazyGroup("department")
    private Department department;
}
