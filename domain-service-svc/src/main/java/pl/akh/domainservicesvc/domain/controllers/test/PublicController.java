package pl.akh.domainservicesvc.domain.controllers.test;

import jakarta.servlet.UnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2User;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.Groups;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/***
 * Test controller user to verify connection between services inside cluster
 */
@RestController
@RequestMapping("/public")
public class PublicController {
    private NotificationService notificationService;

    public PublicController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("ping")
    public String ping() throws Exception {
        Notification notification = new Notification();
        notification.setUserId(UUID.randomUUID());
        notification.setPayload("kafka works in kubernetes");
        notificationService.sendNotification(notification);
        return "pong";
    }
}
