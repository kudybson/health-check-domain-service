package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Address;
import pl.akh.model.rs.AddressRS;

public class AddressMapper {
    public static AddressRS mapToDto(Address address) {
        if (address == null) return null;
        return AddressRS.builder()
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .post(address.getPost())
                .apartmentNumber(address.getApartmentNumber())
                .county(address.getCounty())
                .city(address.getCity())
                .province(address.getProvince())
                .street(address.getStreet())
                .houseNumber(address.getHouseNumber())
                .build();

    }
}
