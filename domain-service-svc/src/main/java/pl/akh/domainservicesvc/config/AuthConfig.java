package pl.akh.domainservicesvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import pl.akh.domainservicesvc.utils.oauth.OAuthDataFacade;

@Configuration
public class AuthConfig {

    @RequestScope
    @Bean
    public OAuthDataFacade authDataFacade() {
        return new OAuthDataFacade();
    }
}
