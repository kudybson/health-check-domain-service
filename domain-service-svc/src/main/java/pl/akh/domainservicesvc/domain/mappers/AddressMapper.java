package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Address;
import pl.akh.model.rq.AddressRQ;
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

    public static Address mapToEntity(Address address, AddressRQ addressRQ) {
        address.setProvince(addressRQ.getProvince());
        address.setCountry(addressRQ.getCountry());
        address.setPost(addressRQ.getPost());
        address.setStreet(addressRQ.getStreet());
        address.setPostalCode(addressRQ.getPostalCode());
        address.setHouseNumber(addressRQ.getHouseNumber());
        address.setApartmentNumber(addressRQ.getApartmentNumber());
        address.setCounty(addressRQ.getCounty());
        address.setCity(addressRQ.getCity());
        return address;
    }

    public static Address mapToEntity(AddressRQ addressRQ) {
        Address address = new Address();
        return mapToEntity(address, addressRQ);
    }
}
