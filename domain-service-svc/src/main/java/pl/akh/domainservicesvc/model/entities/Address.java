package pl.akh.domainservicesvc.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.aspectj.lang.annotation.DeclareAnnotation;

@Entity(name = "ADDRESS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID")
    private Long id;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STREET")
    private String street;

    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Column(name = "APARTMENT_NUMBER")
    private String apartmentNumber;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "POST")
    private String post;

    @Column(name = "COUNTY")
    private String county;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "COUNTRY")
    private String country;
}
