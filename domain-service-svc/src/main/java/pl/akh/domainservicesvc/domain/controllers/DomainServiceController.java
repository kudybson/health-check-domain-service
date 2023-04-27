package pl.akh.domainservicesvc.domain.controllers;

import jakarta.security.auth.message.AuthException;
import pl.akh.domainservicesvc.domain.services.AccessGuard;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;

public abstract class DomainServiceController {
    protected final AuthDataExtractor authDataExtractor;
    private final AccessGuard accessGuard;

    public DomainServiceController(AuthDataExtractor authDataExtractor, AccessGuard accessGuard) {
        this.authDataExtractor = authDataExtractor;
        this.accessGuard = accessGuard;
    }

    protected boolean hasAdministrativeAccessToDepartment(Long departmentId) {
        try {
            return accessGuard.hasAccessAdministrationAccessToDepartment(authDataExtractor.getId(), departmentId)
                    || authDataExtractor.getRoles().contains("ROLE_SUPERADMIN");
        } catch (AuthException e) {
            return false;
        }
    }

    protected boolean hasReceptionistAccessToDepartment(Long departmentId) {
        try {
            return accessGuard.hasReceptionistAccessToDepartment(authDataExtractor.getId(), departmentId)
                    || authDataExtractor.getRoles().contains("ROLE_SUPERADMIN");
        } catch (AuthException e) {
            return false;
        }
    }
}
