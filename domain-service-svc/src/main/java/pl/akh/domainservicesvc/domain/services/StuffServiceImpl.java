package pl.akh.domainservicesvc.domain.services;

import jakarta.servlet.UnavailableException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.Groups;
import pl.akh.model.rq.AdministratorRQ;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2User;
import pl.akh.model.rq.DoctorRQ;
import pl.akh.model.rq.ReceptionistRQ;

import javax.activation.UnsupportedDataTypeException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class StuffServiceImpl {
    private final OAuth2Service oAuth2Service;

    public StuffServiceImpl(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    public UUID addStuffMember(Object stuffMember) throws Exception {
        OAuth2User oauth2User;
        if (stuffMember instanceof AdministratorRQ) {
            oauth2User = mapToOauthUser((AdministratorRQ) stuffMember);
        } else if (stuffMember instanceof ReceptionistRQ) {
            oauth2User = mapToOauthUser((ReceptionistRQ) stuffMember);
        } else if (stuffMember instanceof DoctorRQ) {
            oauth2User = mapToOauthUser((DoctorRQ) stuffMember);
        } else {
            throw new UnsupportedDataTypeException();
        }
        try {
            oAuth2Service.createUser(oauth2User);
            return oAuth2Service.getUUIDByUsername(oauth2User.getUsername()).orElseThrow();
        } catch (UnavailableException e) {
            log.error("Authorization service unavailable", e);
            throw e;
        }
    }

    public void disableStaffMember(UUID staffMemberUUID) {

    }

    public void enableStaffMember(UUID staffMemberUUID) {

    }

    private OAuth2User mapToOauthUser(AdministratorRQ administratorRQ) {
        return OAuth2User.builder()
                .username(administratorRQ.getUsername())
                .firstName(administratorRQ.getFirstName())
                .lastName(administratorRQ.getLastName())
                .password(administratorRQ.getPassword())
                .passwordConfirmation(administratorRQ.getPasswordConfirmation())
                .email(administratorRQ.getEmail())
                .groups(List.of(Groups.ADMIN_GROUP))
                .enabled(true)
                .build();
    }

    private OAuth2User mapToOauthUser(ReceptionistRQ receptionistRQ) {
        return OAuth2User.builder()
                .username(receptionistRQ.getUsername())
                .firstName(receptionistRQ.getFirstName())
                .lastName(receptionistRQ.getLastName())
                .password(receptionistRQ.getPassword())
                .passwordConfirmation(receptionistRQ.getPasswordConfirmation())
                .email(receptionistRQ.getEmail())
                .groups(List.of(Groups.RECEPTIONIST_GROUP))
                .enabled(true)
                .build();
    }

    private OAuth2User mapToOauthUser(DoctorRQ doctorRQ) {
        return OAuth2User.builder()
                .username(doctorRQ.getUsername())
                .firstName(doctorRQ.getFirstName())
                .lastName(doctorRQ.getLastName())
                .password(doctorRQ.getPassword())
                .passwordConfirmation(doctorRQ.getPasswordConfirmation())
                .email(doctorRQ.getEmail())
                .groups(List.of(Groups.DOCTOR_GROUP))
                .enabled(true)
                .build();
    }
}
