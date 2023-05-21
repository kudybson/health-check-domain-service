package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Referral;
import pl.akh.model.rs.ReferralRS;

public class ReferralMapper {

    public static ReferralRS mapToDto(Referral referral) {
        if (referral == null) return null;
        return ReferralRS.builder()
                .id(referral.getId())
                .treatment(TreatmentMapper.mapToDto(referral.getTreatment()))
                .expirationDate(referral.getExpirationDate())
                .testType(TestTypeMapper.toDto(referral.getTestType()))
                .build();
    }
}
