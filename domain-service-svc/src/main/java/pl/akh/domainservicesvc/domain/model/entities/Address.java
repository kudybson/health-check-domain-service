package pl.akh.domainservicesvc.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SelectBeforeUpdate;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ADDRESS")
@NoArgsConstructor
@Getter
@Setter
@SelectBeforeUpdate(value = false)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq_generator")
    @SequenceGenerator(name = "address_seq_generator", sequenceName = "address_seq", allocationSize = 1)
    @Column(name = "ADDRESS_ID", unique = true)
    private Long id;

    @Column(name = "CITY")
    @NotEmpty
    private String city;

    @Column(name = "STREET")
    private String street;

    @Column(name = "HOUSE_NUMBER")
    @NotEmpty
    private String houseNumber;

    @Column(name = "APARTMENT_NUMBER")
    private String apartmentNumber;

    @Column(name = "POSTAL_CODE")
    @NotEmpty
    private String postalCode;

    @Column(name = "POST")
    @NotEmpty
    private String post;

    @Column(name = "COUNTY")
    @NotEmpty
    private String county;

    @Column(name = "PROVINCE")
    @NotEmpty
    private String province;

    @Column(name = "COUNTRY")
    @NotEmpty
    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id.equals(address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
