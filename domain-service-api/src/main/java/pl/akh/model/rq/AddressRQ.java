package pl.akh.model.rq;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRQ {
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @NotBlank
    private String houseNumber;
    @Nullable
    private String apartmentNumber;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String post;
    @NotBlank
    private String county;
    @NotBlank
    private String province;
}
