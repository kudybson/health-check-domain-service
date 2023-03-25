package pl.akh.domainservicesvc.controllers;

import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.akh.domainservicesvc.utils.oauth.OAuthDataExtractorFacade;
import pl.akh.domainservicesvc.utils.roles.HasAnyRole;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class Ping {

    private final OAuthDataExtractorFacade oAuthDataExtractorFacade;

    @Autowired
    public Ping(OAuthDataExtractorFacade oAuthDataExtractorFacade) {
        this.oAuthDataExtractorFacade = oAuthDataExtractorFacade;
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
        return stringBuilder.toString();
    }
}
