package pl.akh.domainservicesvc.controllers;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.keycloak.KeycloakClient;
import pl.akh.domainservicesvc.utils.oauth.OAuthDataExtractorFacade;
import pl.akh.domainservicesvc.utils.roles.HasAnyRole;
import pl.akh.model.common.Groups;
import pl.akh.model.rq.CreateUserRQ;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@Slf4j
@RequestMapping("/")
public class Ping {

    private final OAuthDataExtractorFacade oAuthDataExtractorFacade;
    private final NotificationService notificationService;

    private final OAuth2Service keycloakClient;

    @Autowired
    public Ping(OAuthDataExtractorFacade oAuthDataExtractorFacade, NotificationService notificationService, OAuth2Service keycloakClient) {
        this.oAuthDataExtractorFacade = oAuthDataExtractorFacade;
        this.notificationService = notificationService;
        this.keycloakClient = keycloakClient;
    }

    @GetMapping("ping")
    @HasAnyRole
    public String pong() throws AuthException {
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

        Random random = new Random();
        int i = random.nextInt();
        CreateUserRQ build = CreateUserRQ.builder()
                .username("testUser" + i)
                .firstName("FirstName")
                .lastName("LastName")
                .password("password")
                .email("testUser" + i + "@test.com")
                .groups(List.of(Groups.ADMIN_GROUP))
                .enabled(true)
                .build();
        try {
            keycloakClient.createUser(build);
        } catch (UnavailableException e) {
            log.error("e", e);
        }

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
