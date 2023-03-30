package pl.akh.domainservicesvc.controllers;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.akh.domainservicesvc.utils.oauth.OAuthDataExtractorFacade;
import pl.akh.domainservicesvc.utils.roles.HasAnyRole;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/")
public class Ping {

    private final OAuthDataExtractorFacade oAuthDataExtractorFacade;
    private final NotificationService notificationService;

    @Autowired
    public Ping(OAuthDataExtractorFacade oAuthDataExtractorFacade, NotificationService notificationService) {
        this.oAuthDataExtractorFacade = oAuthDataExtractorFacade;
        this.notificationService = notificationService;
    }

    @GetMapping("ping")
    @HasAnyRole
    public String pong(Principal principal) throws AuthException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(oAuthDataExtractorFacade.getId());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataExtractorFacade.getUsername());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataExtractorFacade.getEmail());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataExtractorFacade.getFullName());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataExtractorFacade.getLastname());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataExtractorFacade.getFirstName());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataExtractorFacade.getRoles());
        stringBuilder.append("\n");

        Notification wizytaOdwolana = Notification.builder()
                .userId(oAuthDataExtractorFacade.getId())
                .payload("Wizyta odwolana")
                .build();
        try {
            notificationService.sendNotification(wizytaOdwolana);
        } catch (Exception e) {
            log.info("Error during sending notification", e);
        }
        return stringBuilder.toString();
    }
}
