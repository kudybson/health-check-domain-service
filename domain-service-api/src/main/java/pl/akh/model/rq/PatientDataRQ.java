package pl.akh.model.rq;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.akh.model.common.Gender;
import pl.akh.utils.AddressConstraint;

@Data
@Builder
public class PatientDataRQ {
    @PESEL
    @Nullable
    private String pesel;

    @NotBlank
    private String phoneNumber;
    @NotNull
    private Gender gender;

    @AddressConstraint
    private AddressRQ addressRQ;

}
