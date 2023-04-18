package pl.akh.domainservicesvc.domain.controllers;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;
import pl.akh.domainservicesvc.domain.utils.roles.HasAnyRole;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

@RestController
@Slf4j
@RequestMapping("/")
public class Ping {

    private final AuthDataExtractor authDataExtractor;
    private final NotificationService notificationService;
    private final OAuth2Service keycloakClient;

    @Autowired
    public Ping(AuthDataExtractor authDataExtractor, NotificationService notificationService, OAuth2Service keycloakClient) {
        this.authDataExtractor = authDataExtractor;
        this.notificationService = notificationService;
        this.keycloakClient = keycloakClient;
    }

    @GetMapping("ping")
    @HasAnyRole
    public String pong() throws AuthException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(authDataExtractor.getId());
        stringBuilder.append("\n");
        stringBuilder.append(authDataExtractor.getUsername());
        stringBuilder.append("\n");
        stringBuilder.append(authDataExtractor.getEmail());
        stringBuilder.append("\n");
        stringBuilder.append(authDataExtractor.getFullName());
        stringBuilder.append("\n");
        stringBuilder.append(authDataExtractor.getLastname());
        stringBuilder.append("\n");
        stringBuilder.append(authDataExtractor.getFirstName());
        stringBuilder.append("\n");
        stringBuilder.append(authDataExtractor.getRoles());
        stringBuilder.append("\n");

        Notification wizytaOdwolana = Notification.builder()
                .userId(authDataExtractor.getId())
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
