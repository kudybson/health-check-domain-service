package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@SelectBeforeUpdate(value=false)
@PrimaryKeyJoinColumn(name = "ADMINISTRATOR_ID")
public class Administrator extends Person implements Serializable {

    private static final long serialVersionUID = 2L;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID")
    @LazyGroup("department")
    private Department department;

}
