package pl.akh.domainservicesvc.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import pl.akh.domainservicesvc.domain.utils.auth.oauth.OAuthDataExtractorFacade;

@Configuration
public class AuthConfig {

    @RequestScope
    @Bean
    public OAuthDataExtractorFacade authDataFacade() {
        return new OAuthDataExtractorFacade();
    }
}
