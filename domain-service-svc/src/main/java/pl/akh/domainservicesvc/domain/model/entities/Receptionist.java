package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @ManyToOne(cascade = CascadeType.DETACH, targetEntity = Department.class)
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID", nullable = false)
    @NotNull
    private Department department;
}
