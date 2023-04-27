package pl.akh.domainservicesvc.config.auth;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/departments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/doctors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/schedules/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/medical-tests-schedules/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(new CustomJwtAuthenticationConverter());
        http.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors().configurationSource(corsConfigurationSource()).and()
                .csrf().disable()
                .anonymous().and()
                .headers()
                .xssProtection().and()
                .contentSecurityPolicy("script-src 'self'");
        return http.build();
    }

    private class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        @Override
        public AbstractAuthenticationToken convert(final Jwt jwt) {

            Collection<GrantedAuthority> authorities = new ArrayList<>();

            jwt.getClaim("realm_access");
            LinkedTreeMap<String, List<String>> realmAccess = (LinkedTreeMap<String, List<String>>) jwt.getClaims().get("realm_access");
            List<String> roles = realmAccess.get("roles");
            // Get a unique id from jwt and search for the user in your database

            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            return new JwtAuthenticationToken(jwt, authorities);
        }
    }

    private CorsConfigurationSource corsConfigurationSource() {
        // Very permissive CORS config...
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        // Limited to API routes (neither actuator nor Swagger-UI)
        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}
