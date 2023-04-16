package pl.akh.domainservicesvc.domain.controllers;

import jakarta.servlet.UnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.CreateOauth2User;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.Groups;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/***
 * Test controller user to verify connection between services inside cluster
 */
@RestController
@RequestMapping("/public")
public class PublicController {
    OAuth2Service oAuth2Service;

    @Autowired
    public PublicController(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @GetMapping("ping")
    public String ping() throws UnavailableException {
        Random random = new Random();
        CreateOauth2User build = CreateOauth2User.builder()
                .username("useeeee" + random.nextInt())
                .groups(List.of(Groups.DOCTOR_GROUP))
                .firstName("first")
                .lastName("last")
                .email("jakis.email" + random.nextInt() + "@wp.pl")
                .enabled(true)
                .password("password")
                .passwordConfirmation("password")
                .build();

        oAuth2Service.createUser(build);
        return "pong";
    }
}
