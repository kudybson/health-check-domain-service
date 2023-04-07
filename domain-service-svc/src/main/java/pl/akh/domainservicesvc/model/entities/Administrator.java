package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;

@Entity(name = "ADMINISTRATOR")
@NoArgsConstructor
@Getter
@Setter
public class Administrator extends Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(mappedBy = "administrator", cascade = CascadeType.DETACH)
    @LazyGroup("department")
    private Department department;
}
