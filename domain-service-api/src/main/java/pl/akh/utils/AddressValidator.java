package pl.akh.utils;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;
import java.util.stream.Stream;

public class AddressValidator implements ConstraintValidator<AddressConstraint, pl.akh.model.rq.Address> {
    @Override
    public boolean isValid(pl.akh.model.rq.Address address, ConstraintValidatorContext constraintValidatorContext) {
        String country = address.getCountry();
        if (!CountryUtils.getAvailableCountriesCodes().contains(country)) {
            return false;
        }
        String postalCode = address.getPostalCode();
        if (postalCode == null || !postalCode.matches(CountryUtils.getPostCodePatternForCountry(country))) {
            return false;
        }
        return Stream.of(
                        address.getCity(),
                        address.getCounty(),
                        address.getProvince(),
                        address.getStreet(),
                        address.getHouseNumber(),
                        address.getApartmentNumber(),
                        address.getPost())
                .noneMatch(StringUtils::isBlank);
    }
}
