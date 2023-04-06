package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;

import java.io.Serializable;

@Entity
@Table(name = "RECEPTIONIST")
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "RECEPTIONIST_ID")
public class Receptionist extends Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = CascadeType.DETACH)
    @LazyGroup("department")
    private Department department;
}
