package pl.akh.utils;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.akh.model.rq.AddressRQ;

import java.util.stream.Stream;

public class AddressValidator implements ConstraintValidator<AddressConstraint, AddressRQ> {
    @Override
    public boolean isValid(AddressRQ addressRQ, ConstraintValidatorContext constraintValidatorContext) {
        if (addressRQ == null) {
            return false;
        }
        String country = addressRQ.getCountry();
        if (!CountryUtils.getAvailableCountriesCodes().contains(country)) {
            return false;
        }
        String postalCode = addressRQ.getPostalCode();
        if (postalCode == null || !postalCode.matches(CountryUtils.getPostCodePatternForCountry(country))) {
            return false;
        }
        return Stream.of(
                        addressRQ.getCity(),
                        addressRQ.getCounty(),
                        addressRQ.getProvince(),
                        addressRQ.getStreet(),
                        addressRQ.getHouseNumber(),
                        addressRQ.getApartmentNumber(),
                        addressRQ.getPost())
                .noneMatch(StringUtils::isBlank);
    }
}
