package pl.akh.domainservicesvc.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.akh.domainservicesvc.utils.oauth.OAuthDataFacade;
import pl.akh.domainservicesvc.utils.roles.HasAnyRole;
import pl.akh.domainservicesvc.utils.roles.HasRolePatient;

import java.security.Principal;

//@RolesAllowed(value = "ROLE_ADMIN")
@RestController
@RequestMapping("/")
public class Ping {

    private final OAuthDataFacade oAuthDataFacade;

    @Autowired
    public Ping(OAuthDataFacade oAuthDataFacade) {
        this.oAuthDataFacade = oAuthDataFacade;
    }

    @GetMapping("ping")
    @HasAnyRole
    public String pong(Principal principal) throws AuthException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(oAuthDataFacade.getId());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataFacade.getEmail());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataFacade.getFullName());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataFacade.getLastname());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataFacade.getFirstName());
        stringBuilder.append("\n");
        stringBuilder.append(oAuthDataFacade.getRoles());
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
