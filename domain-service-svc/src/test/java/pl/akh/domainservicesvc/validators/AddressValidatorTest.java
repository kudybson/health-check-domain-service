package pl.akh.domainservicesvc.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.akh.model.rq.Address;
import pl.akh.utils.AddressValidator;

public class AddressValidatorTest {
    @Test
    public void addressShouldBeValid() {
        Address address = Address.builder()
                .country("PL")
                .postalCode("12-123")
                .post("Krakow Lobzow")
                .province("Malopolskie")
                .county("Powiat krakowski")
                .city("Krakow")
                .street("Wybickiego")
                .apartmentNumber("106")
                .houseNumber("56")
                .build();
        AddressValidator addressValidator = new AddressValidator();
        Assertions.assertTrue(addressValidator.isValid(address, null));
    }

    @Test
    public void postalCodeShouldNotBeValid() {
        Address address = Address.builder()
                .country("PL")
                .postalCode("12-1231414")
                .post("Krakow Lobzow")
                .province("Malopolskie")
                .county("Powiat krakowski")
                .city("Krakow")
                .street("Wybickiego")
                .apartmentNumber("106")
                .houseNumber("56")
                .build();
        AddressValidator addressValidator = new AddressValidator();
        Assertions.assertFalse(addressValidator.isValid(address, null));
    }

    @Test
    public void incompleteAddressShouldNotBeValid() {
        Address address = Address.builder()
                .country("PL")
                .postalCode("12-123")
                .post("Krakow Lobzow")
                .province("Malopolskie")
                .county("Powiat krakowski")
                .street("Wybickiego")
                .apartmentNumber("106")
                .houseNumber("56")
                .build();
        AddressValidator addressValidator = new AddressValidator();
        Assertions.assertFalse(addressValidator.isValid(address, null));
    }
}
