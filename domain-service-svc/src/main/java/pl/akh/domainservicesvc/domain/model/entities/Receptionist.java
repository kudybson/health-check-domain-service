package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.io.Serializable;

@Entity(name = "RECEPTIONIST")
@NoArgsConstructor
@Getter
@Setter
@SelectBeforeUpdate(value = false)
@PrimaryKeyJoinColumn(name = "RECEPTIONIST_ID")
public class Receptionist extends Person implements Serializable {

    private static final long serialVersionUID = 9L;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    @LazyGroup("department")
    private Department department;
}
