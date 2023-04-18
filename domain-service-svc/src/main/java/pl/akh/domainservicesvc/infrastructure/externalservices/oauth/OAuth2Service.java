package pl.akh.domainservicesvc.infrastructure.externalservices.oauth;

import jakarta.servlet.UnavailableException;

import java.util.UUID;

public interface OAuth2Service {
    boolean createUser(CreateOauth2User createOauth2User) throws UnavailableException;

    UUID getUUIDByUsername(String username) throws UnavailableException;
}
